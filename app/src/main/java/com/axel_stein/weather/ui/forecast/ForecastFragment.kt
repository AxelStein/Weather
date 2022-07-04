package com.axel_stein.weather.ui.forecast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.axel_stein.weather.R
import com.axel_stein.weather.data.entity.Forecast
import com.axel_stein.weather.databinding.FragmentForecastBinding
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@AndroidEntryPoint
class ForecastFragment : Fragment() {
    private var _binding: FragmentForecastBinding? = null
    private val binding get() = _binding!!
    private val adapter = ForecastAdapter()
    private val viewModel: ForecastViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val city = arguments?.getString("city") ?: ""
        val countryCode = arguments?.getString("country") ?: ""

        viewModel.setCity(city, countryCode)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForecastBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.forecastsRecyclerView.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        binding.forecastsRecyclerView.adapter = adapter

        viewModel.forecasts.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        viewModel.currentForecast.observe(viewLifecycleOwner) {
            setCurrentForecast(it)
        }
        adapter.onItemClickListener = {
            viewModel.setCurrent(it)
        }
        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.progressLayout.isVisible = it
        }
        viewModel.isNoDataShown.observe(viewLifecycleOwner) {
            showErrorMessage(it, R.string.no_data)
        }
        viewModel.isErrorShown.observe(viewLifecycleOwner) {
            showErrorMessage(it, R.string.error)
        }
        viewModel.isConnectionErrorShown.observe(viewLifecycleOwner) {
            if (it) {
                Snackbar.make(binding.root, R.string.connection_error, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun showErrorMessage(isShown: Boolean, @StringRes messageRes: Int) = with(binding) {
        errorLayout.isVisible = isShown
        errorMessage.setText(messageRes)
    }

    private fun setCurrentForecast(forecast: Forecast?) = with(binding) {
        city.text = forecast?.city
        dateTime.text = forecast?.date?.let {
            if (it == LocalDate.now()) {
                getString(R.string.today)
            } else {
                it.format(DateTimeFormatter.ofPattern("d MMM, yyyy", Locale.US))
            }
        }
        temp.text = getString(R.string.temp, forecast?.temp)
        summary.text = forecast?.summary

        Glide.with(icon)
            .load(forecast?.iconUrl)
            .error(R.drawable.ic_error_24dp)
            .into(icon)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
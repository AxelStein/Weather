package com.axel_stein.weather.ui.forecast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.axel_stein.weather.R
import com.axel_stein.weather.data.entity.Forecast
import com.axel_stein.weather.databinding.FragmentForecastBinding
import com.bumptech.glide.Glide
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class ForecastFragment : Fragment() {
    private var _binding: FragmentForecastBinding? = null
    private val binding get() = _binding!!
    private val adapter = ForecastAdapter()
    private val viewModel: ForecastViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getString("city")?.let {
            viewModel.setCity(it)
        }
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
            showForecastTitle()
        }
        viewModel.currentForecast.observe(viewLifecycleOwner) {
            setCurrentForecast(it)
        }
        adapter.onItemClickListener = {
            viewModel.setCurrent(it)
        }
        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = it
        }
        viewModel.isNoDataShown.observe(viewLifecycleOwner) {
            binding.noData.isVisible = it
        }
    }

    private fun showForecastTitle(isShown: Boolean = true) = with(binding) {
        divider.isVisible = isShown
        forecastTitle.isVisible = isShown
    }

    private fun setCurrentForecast(forecast: Forecast?) = with(binding) {
        city.text = forecast?.city
        dateTime.text = forecast?.dateTime?.let {
            if (it == LocalDate.now()) {
                getString(R.string.today)
            } else {
                it.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
            }
        }
        temp.text = getString(R.string.temp, forecast?.temp)
        summary.text = forecast?.summary

        Glide.with(icon)
            .load(forecast?.iconUrl)
            .into(icon)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
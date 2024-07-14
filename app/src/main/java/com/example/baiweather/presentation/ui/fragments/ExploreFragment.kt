package com.example.baiweather.presentation.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.JsonReader
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.baiweather.data.remote.CityJson
import com.example.baiweather.data.remote.CurrentWeatherDto
import com.example.baiweather.databinding.FragmentExploreBinding
import com.example.baiweather.domain.util.Resource
import com.example.baiweather.presentation.viewModels.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.util.Timer
import java.util.TimerTask


@AndroidEntryPoint
class ExploreFragment : Fragment() {

    private var _binding: FragmentExploreBinding? = null
    lateinit var cities: List<CityJson>
    private val binding get() = _binding!!

    private val viewModel by hiltNavGraphViewModels<WeatherViewModel>(com.example.baiweather.R.id.main_nav_graph)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExploreBinding.inflate(inflater, container, false)
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                cities = withContext(Dispatchers.IO) {
                    readJsonStream(requireContext().assets.open("city.list.json"))
                }

            } catch (e: Exception) {
                Log.e("ExploreFragment", "Error searching city", e)
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listeners()
        observers()
    }


    private fun searchCity(query: String): List<String> {
        val list = mutableListOf<String>()
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val city = query
                cities.forEach {
                    if(it.name.contains(city) && !list.contains(it.name)){
                        list.add(it.name)
                        Log.d("returnlist 1"," ${list}")
                    }
                }
            } catch (e: Exception) {
                Log.e("ExploreFragment", "Error searching city", e)
            }
        }
        Log.d("returnlist"," ${list}")
        return list
    }

    @Throws(IOException::class)
    fun readJsonStream(stream: InputStream?): List<CityJson> {
        val reader: JsonReader = JsonReader(InputStreamReader(stream, "UTF-8"))
        try {
            return readCitiesArray(reader)
        } finally {
            reader.close()
        }
    }

    @Throws(IOException::class)
    fun readCitiesArray(reader: JsonReader): List<CityJson> {
        val cities: MutableList<CityJson> = ArrayList<CityJson>()

        reader.beginArray()
        while (reader.hasNext()) {
            cities.add(readCity(reader))
        }
        reader.endArray()
        return cities
    }

    @Throws(IOException::class)
    fun readCity(reader: JsonReader): CityJson {
        var id: Int = -1
        var cityName: String? = ""
        var country: String? = ""
        var coord: CityJson.Coordinates? = CityJson.Coordinates()

        reader.beginObject()
        while (reader.hasNext()) {
            val name: String = reader.nextName()
            when (name) {
                "id" -> {
                    id = reader.nextInt()
                }
                "name" -> {
                    cityName = reader.nextString()
                }
                "country" -> {
                    country = reader.nextString()
                }
                "coord" -> {
                    coord = readCoordinates(reader)
                }
                else -> {
                    reader.skipValue()
                }
            }
        }
        reader.endObject()
        return CityJson(id, cityName?:"", country?:"", coord?:CityJson.Coordinates())
    }

    @Throws(IOException::class)
    fun readCoordinates(reader: JsonReader): CityJson.Coordinates {
        var lon: Double? = null
        var lat: Double? = null

        reader.beginObject()
        while (reader.hasNext()) {
            val name: String = reader.nextName()
            if (name == "lon") {
                lon = reader.nextDouble()
            } else if (name == "lat") {
                lat = reader.nextDouble()
            } else {
                reader.skipValue()
            }
        }
        reader.endObject()
        return CityJson.Coordinates(lon, lat)
    }


    private fun listeners() {
//        binding.etSearch.doOnTextChanged { text, start, before, count ->
//            viewLifecycleOwner.lifecycleScope.launch {
//                Delay(1000)
//            }
//            viewModel.getWeatherByCity(text.toString())
//        }

        binding.etSearch.addTextChangedListener(
            object : TextWatcher {
                private var timer = Timer()
                private val DELAY: Long = 1000
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun afterTextChanged(s: Editable) {
                    timer.cancel()
                    timer = Timer()
                    timer.schedule(
                        object : TimerTask() {
                            override fun run() {
                                val list = searchCity(s.toString())
                                Log.d("searchCity", list.toString())

//                                viewModel.getWeatherByCity(s.toString())
                            }
                        },
                        DELAY
                    )
                }
            }
        )

    }

    private fun observers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.cityWeatherState.collect {
                    when (it) {
                        is Resource.Error -> {
                            Log.d(
                                "getWeatherByCity error",
                                "mess - ${it.message.toString()}  data  - ${it.data.toString()}"
                            )

                            if (it.message!!.isNotBlank()) {
                                binding.tvError.text = it.message.toString()
                                binding.tvError.visibility = View.VISIBLE
                            } else {
                                binding.tvError.visibility = View.GONE
                            }
                            binding.progressBar.visibility = View.GONE
                        }

                        is Resource.Success -> {
                            Log.d("getWeatherByCity success", it.data.toString())
//                            setUpUI(it.data)
                            binding.progressBar.visibility = View.GONE
                            binding.tvError.visibility = View.GONE
                        }

                        is Resource.Loading -> {
                            Log.d("getWeatherByCity ", "loading")
                            binding.progressBar.visibility = View.VISIBLE
                            binding.tvError.visibility = View.GONE
                        }

                        else -> {}
                    }
                }
            }
        }
    }

    private fun setUpUI(data: CurrentWeatherDto) = with(binding) {

    }
}
package com.example.threads

import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.threads.databinding.FragmentTimerBinding

class TimerFragment : Fragment() {
    private var currentTime = 0
    private var isTimerRunning = false
    private var timerTask: TimerTask? = null

    private var _binding: FragmentTimerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTimerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnStart.setOnClickListener {
            startTimer()
            isTimerRunning = true
        }
        binding.btnStop.setOnClickListener {
            stopTimer()
            isTimerRunning = false
        }
        binding.btnRestart.setOnClickListener {
            currentTime = 0
            binding.tvText.text = String.format("%s:%02d", "00", currentTime)
            updateProgressBar()
        }
    }

    private fun startTimer() {
        if (timerTask == null || timerTask?.status == AsyncTask.Status.FINISHED) {
            timerTask = TimerTask()
            timerTask?.execute()
        }
    }

    private fun stopTimer() {
        timerTask?.cancel(true)
    }

    private fun updateProgressBar() {
        binding.progressBar.progress = currentTime
    }

    inner class TimerTask : AsyncTask<Void, Int, Void>() {

        override fun doInBackground(vararg params: Void?): Void? {
            while (currentTime <= 60 && isTimerRunning) {
                currentTime++
                updateProgressBar()

                publishProgress(currentTime)

                if (currentTime == 60) {
                    currentTime = 0
                    isTimerRunning = false
                    publishProgress(currentTime)
                    updateProgressBar()
                }
                try {
                    Thread.sleep(250)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
            return null
        }

        override fun onProgressUpdate(vararg values: Int?) {
            binding.tvText.text = String.format("%s:%02d", "00", values[0])
            super.onProgressUpdate(*values)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
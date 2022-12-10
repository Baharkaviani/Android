package com.example.hafezdivination

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.hafezdivination.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class TextShower : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private var currentText = 0
    private val poems = listOf("اگر آن ترک شیرازی به دست آرد دل ما را\n" +
            "\n" +
            "به خال هندویش بخشم سمرقند و بخارا را\n" +
            "\n" +
            "بده ساقی می باقی که در جنت نخواهی یافت\n" +
            "\n" +
            "کنار آب رکن آباد و گلگشت مصلا را\n" +
            "\n" +
            "فغان کاین لولیان شوخ شیرین کار شهرآشوب\n" +
            "\n" +
            "چنان بردند صبر از دل که ترکان خوان یغما را",
            "دل می\u200Cرود ز دستم صاحب دلان خدا را\n" +
                    "\n" +
                    "دردا که راز پنهان خواهد شد آشکارا\n" +
                    "\n" +
                    "کشتی شکستگانیم ای باد شرطه برخیز\n" +
                    "\n" +
                    "باشد که بازبینم دیدار آشنا را\n" +
                    "\n" +
                    "ده روزه مهر گردون افسانه است و افسون\n" +
                    "\n" +
                    "نیکی به جای یاران فرصت شمار یارا",
            "دل و دینم شد و دلبر به ملامت برخاست\n" +
                    "\n" +
                    "گفت با ما منشین کز تو سلامت برخاست\n" +
                    "\n" +
                    "که شنیدی که در این بزم دمی خوش بنشست\n" +
                    "\n" +
                    "که نه در آخر صحبت به ندامت برخاست\n" +
                    "\n" +
                    "شمع اگر زان لب خندان به زبان لافی زد\n" +
                    "\n" +
                    "پیش عشاق تو شب\u200Cها به غرامت برخاست",
            "بیا که قصر امل سخت سست بنیادست\n" +
                    "\n" +
                    "بیار باده که بنیاد عمر بر بادست\n" +
                    "\n" +
                    "غلام همت آنم که زیر چرخ کبود\n" +
                    "\n" +
                    "ز هر چه رنگ تعلق پذیرد آزادست\n" +
                    "\n" +
                    "چه گویمت که به میخانه دوش مست و خراب\n" +
                    "\n" +
                    "سروش عالم غیبم چه مژده\u200Cها دادست",
            "منم که گوشه میخانه خانقاه من است\n" +
                    "\n" +
                    "دعای پیر مغان ورد صبحگاه من است\n" +
                    "\n" +
                    "گرم ترانه چنگ صبوح نیست چه باک\n" +
                    "\n" +
                    "نوای من به سحر آه عذرخواه من است\n" +
                    "\n" +
                    "ز پادشاه و گدا فارغم بحمدالله\n" +
                    "\n" +
                    "گدای خاک در دوست پادشاه من است")

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.showTextButton.setOnClickListener {
            binding.mainText.text = getRandomPoem()
        }

        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getRandomPoem(): String {
        var tempIndices = mutableListOf(0, 1, 2, 3, 4)
        tempIndices.removeAt(currentText)
        currentText = tempIndices.random()
        return poems[currentText]
    }
}
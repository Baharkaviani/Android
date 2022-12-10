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
    private var _currentText = 0
    private val _poems = listOf("اَلا یا اَیُّهَا السّاقی اَدِرْ کَأسَاً و ناوِلْها\n" +
            "که عشق آسان نمود اوّل ولی افتاد مشکل‌ها\n" +
            "به بویِ نافه‌ای کآخر صبا زان طُرّه بگشاید\n" +
            "ز تابِ جَعدِ مشکینش چه خون افتاد در دل‌ها\n" +
            "مرا در منزلِ جانان چه امنِ عیش چون هر دَم\n" +
            "جَرَس فریاد می‌دارد که بَربندید مَحمِل‌ها\n" +
            "به مِی سجّاده رنگین کن گَرت پیرِ مُغان گوید\n" +
            "که سالِک بی‌خبر نَبوَد ز راه و رسمِ منزل‌ها\n" +
            "شبِ تاریک و بیمِ موج و گردابی چنین هایل\n" +
            "کجا دانند حالِ ما سبکبارانِ ساحل‌ها\n" +
            "همه کارم ز خودکامی به بدنامی کشید آخر\n" +
            "نهان کِی مانَد آن رازی کزو سازند محفل‌ها\n" +
            "حضوری گر همی‌خواهی از او غایب مشو حافظ\n" +
            "مَتٰی ما تَلْقَ مَنْ تَهْویٰ دَعِ الدُّنْیا و اَهْمِلْها"
        ,

            "صلاحِ کار کجا و منِ خراب کجا\n" +
            "ببین تفاوتِ ره کز کجاست تا بکجا\n" +
            "دلم ز صومعه بگرفت و خِرقِهٔ سالوس\n" +
            "کجاست دیرِ مُغان و شرابِ ناب کجا\n" +
            "چه نسبت است به رندی صَلاح و تقوا را\n" +
            "سماعِ وعظ کجا، نغمهٔ رباب کجا\n" +
            "ز رویِ دوست دلِ دشمنان چه دریابد\n" +
            "چراغِ مرده کجا، شمعِ آفتاب کجا\n" +
            "چو کُحلِ بینشِ ما خاکِ آستانِ شماست\n" +
            "کجا رویم؟، بفرما، ازین جناب کجا\n" +
            "مبین به سیبِ زَنَخدان که چاه در راه است\n" +
            "کجا همی روی ای دل بدین شتاب کجا\n" +
            "بشد، که یاد خوشش باد، روزگارِ وصال\n" +
            "خود آن کرشمه کجا رفت و آن عِتاب کجا\n" +
            "قرار و خواب ز حافظ طمع مدار ای دوست\n" +
            "قرار چیست؟ صبوری کدام و خواب کجا؟\n"
        ,

            "اگر آن تُرک شیرازی به دست آرد دل ما را\n" +
            "به خال هِندویَش بخشم سمرقند و بخارا را\n" +
            "بده ساقی مِیِ باقی که در جنت نخواهی یافت\n" +
            "کنار آب رُکن آباد و گُلگَشت مُصَّلا را\n" +
            "فَغان کاین لولیانِ شوخِ شیرین‌کار شهرآشوب\n" +
            "چنان بردند صبر از دل که تُرکان خوان یغما را\n" +
            "ز عشق ناتمام ما جمال یار مُستَغنی است\n" +
            "به آب و رنگ و خال و خط چه حاجت روی زیبا را؟\n" +
            "من از آن حسن روزافزون که یوسف داشت دانستم\n" +
            "که عشق از پردهٔ عصمت برون آرد زلیخا را\n" +
            "اگر دشنام فرمایی و گر نفرین، دعا گویم\n" +
            "جواب تلخ می‌زیبد، لب لعل شکرخا را\n" +
            "نصیحت گوش کن جانا که از جان دوست‌تر دارند\n" +
            "جوانان سعادتمند پند پیر دانا را\n" +
            "حدیث از مطرب و مِی گو و راز دَهر کمتر جو\n" +
            "که کس نگشود و نگشاید به حکمت این معما را\n" +
            "غزل گفتی و دُر سفتی، بیا و خوش بخوان حافظ\n" +
            "که بر نظم تو افشاند فلک عِقد ثریا را\n"
        ,

            "صبا به لطف بگو آن غزال رعنا را\n" +
            "که سر به کوه و بیابان تو داده‌ای ما را\n" +
            "شکرفروش که عمرش دراز باد چرا\n" +
            "تَفَقُّدی نکند طوطی شکرخا را\n" +
            "غرور حُسنت اجازت مگر نداد ای گل؟\n" +
            "که پرسشی نکنی عَندَلیب شیدا را\n" +
            "به خُلق و لطف توان کرد صید اهل نظر\n" +
            "به بند و دام نگیرند مرغ دانا را\n" +
            "ندانم از چه سبب رنگ آشنایی نیست\n" +
            "سَهی قدانِ سیَه چشمِ ماه سیما را\n" +
            "چو با حبیب نشینی و باده پیمایی\n" +
            "به یاد دار مُحِبّان بادپیما را\n" +
            "جز این قَدَر نتوان گفت در جمال تو عیب\n" +
            "که وضع مِهر و وفا نیست روی زیبا را\n" +
            "در آسمان نه عجب گر به گفتهٔ حافظ\n" +
            "سرود زُهره به رقص آورد مسیحا را\n"
        ,

            "ساقیا برخیز و دَردِه جام را\n" +
            "خاک بر سر کن غم ایام را\n" +
            "ساغر می بر کفم نِه تا ز بَر\n" +
            "بَرکِشم این دلق اَزرَق‌فام را\n" +
            "گر چه بدنامی‌ست نزد عاقلان\n" +
            "ما نمی‌خواهیم ننگ و نام را\n" +
            "باده دَردِه چند از این باد غرور\n" +
            "خاک بر سر نفس نافرجام را\n" +
            "دود آه سینهٔ نالان من\n" +
            "سوخت این افسردگان خام را\n" +
            "محرم راز دل شیدای خود\n" +
            "کس نمی‌بینم ز خاص و عام را\n" +
            "با دلارامی مرا خاطر خوش است\n" +
            "کز دلم یک باره بُرد آرام را\n" +
            "ننگرد دیگر به سرو اندر چمن\n" +
            "هرکه دید آن سرو سیم‌اندام را\n" +
            "صبر کن حافظ به سختی روز و شب\n" +
            "عاقبت روزی بیابی کام را\n"
    )

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
        tempIndices.removeAt(_currentText)
        _currentText = tempIndices.random()
        return _poems[_currentText]
    }
}
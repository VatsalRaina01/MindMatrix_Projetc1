package com.shishusneh.data.constants

/**
 * Pre-cached feeding tips and myth-busting Q&A content.
 * Age-contextual tips for breastfeeding, complementary feeding, and maternal nutrition.
 * All content bilingual (English + Hindi).
 */
object FeedingTips {

    data class DailyTip(
        val weekRange: IntRange,
        val titleEn: String,
        val titleHi: String,
        val contentEn: String,
        val contentHi: String,
        val category: String // BREASTFEEDING, COMPLEMENTARY, MATERNAL_NUTRITION
    )

    data class MythBuster(
        val questionEn: String,
        val questionHi: String,
        val answerEn: String,
        val answerHi: String
    )

    val dailyTips: List<DailyTip> = listOf(
        // Week 0–2: Immediate breastfeeding
        DailyTip(0..2, "Start Breastfeeding Within 1 Hour", "1 घंटे के अंदर स्तनपान शुरू करें",
            "Initiate breastfeeding within the first hour of birth. The first milk (colostrum) is yellow and thick — it's packed with antibodies that protect your baby from infections.",
            "जन्म के पहले घंटे में स्तनपान शुरू करें। पहला दूध (कोलोस्ट्रम) पीला और गाढ़ा होता है — इसमें एंटीबॉडी होती हैं जो बच्चे को संक्रमण से बचाती हैं।",
            "BREASTFEEDING"),
        DailyTip(3..4, "Feed On Demand", "जब बच्चा चाहे तब दूध पिलाएं",
            "Feed your baby whenever they show hunger signs — rooting, sucking hands, or fussing. Newborns typically feed 8–12 times in 24 hours.",
            "जब भी बच्चा भूख के संकेत दिखाए — मुँह खोलना, हाथ चूसना — तब दूध पिलाएं। नवजात 24 घंटे में 8–12 बार दूध पीते हैं।",
            "BREASTFEEDING"),
        DailyTip(5..6, "Growth Spurt — Feed More", "विकास तेज़ी — ज़्यादा दूध पिलाएं",
            "Around 6 weeks, babies go through a growth spurt. They may want to feed more frequently — this is normal! It helps increase your milk supply.",
            "लगभग 6 सप्ताह में बच्चे तेज़ी से बढ़ते हैं। वे ज़्यादा बार दूध माँग सकते हैं — यह सामान्य है! इससे दूध की मात्रा बढ़ती है।",
            "BREASTFEEDING"),
        DailyTip(7..8, "Proper Latch Is Key", "सही तरीके से पकड़ना ज़रूरी",
            "Ensure baby's mouth covers most of the dark area around your nipple (areola), not just the nipple tip. A good latch prevents soreness and ensures baby gets enough milk.",
            "बच्चे का मुँह निप्पल के आसपास के काले हिस्से (एरिओला) को ढके, सिर्फ निप्पल की नोक को नहीं। सही पकड़ से दर्द नहीं होता और बच्चे को पूरा दूध मिलता है।",
            "BREASTFEEDING"),
        DailyTip(9..12, "Mother's Nutrition Matters", "माँ का पोषण ज़रूरी है",
            "Eat a balanced diet with extra calories (about 500 kcal/day more). Include dal, green vegetables, milk, fruits, and plenty of water. Your nutrition directly affects milk quality.",
            "संतुलित आहार लें, अतिरिक्त कैलोरी (लगभग 500 किलो कैलोरी/दिन) के साथ। दाल, हरी सब्ज़ियाँ, दूध, फल और खूब पानी पिएं। आपका पोषण सीधे दूध की गुणवत्ता प्रभावित करता है।",
            "MATERNAL_NUTRITION"),
        DailyTip(13..16, "No Water Needed Before 6 Months", "6 महीने तक पानी की ज़रूरत नहीं",
            "Breast milk is 88% water — even in hot weather, your baby doesn't need extra water before 6 months. Giving water can reduce milk intake and cause infections.",
            "माँ का दूध 88% पानी होता है — गर्मी में भी 6 महीने से पहले बच्चे को अलग से पानी नहीं चाहिए। पानी देने से दूध कम पीता है और संक्रमण हो सकता है।",
            "BREASTFEEDING"),
        DailyTip(17..20, "Both Breasts Each Feeding", "हर बार दोनों स्तनों से पिलाएं",
            "Try to offer both breasts during each feeding session. Start with the breast you finished with last time. This helps maintain milk supply in both breasts.",
            "हर बार दोनों स्तनों से दूध पिलाने की कोशिश करें। पिछली बार जिस स्तन से खत्म किया, इस बार उससे शुरू करें।",
            "BREASTFEEDING"),
        DailyTip(21..24, "Preparing for Solid Foods", "ठोस आहार की तैयारी",
            "Around 6 months, your baby will be ready for solid foods alongside breastfeeding. Signs of readiness: sitting with support, showing interest in food, loss of tongue-thrust reflex.",
            "लगभग 6 महीने में बच्चा स्तनपान के साथ ठोस आहार के लिए तैयार होगा। तैयारी के संकेत: सहारे से बैठना, खाने में रुचि, जीभ से बाहर धकेलना बंद।",
            "COMPLEMENTARY"),
        DailyTip(25..28, "Start With Single Grain Cereal", "एक अनाज से शुरू करें",
            "Begin with iron-fortified rice or ragi cereal mixed with breast milk. Offer 1–2 teaspoons once a day. Continue breastfeeding — solid food is complementary, not a replacement.",
            "माँ के दूध में मिलाकर चावल या रागी का दलिया दें। दिन में एक बार 1–2 चम्मच। स्तनपान जारी रखें — ठोस आहार पूरक है, विकल्प नहीं।",
            "COMPLEMENTARY"),
        DailyTip(29..32, "Introduce Vegetables & Fruits", "सब्ज़ियाँ और फल शुरू करें",
            "Gradually introduce mashed vegetables (potato, carrot, pumpkin) and fruits (banana, apple). Introduce one new food every 3–4 days to watch for allergies.",
            "धीरे-धीरे मसली सब्ज़ियाँ (आलू, गाजर, कद्दू) और फल (केला, सेब) दें। हर 3–4 दिन में एक नया खाना शुरू करें ताकि एलर्जी पहचान सकें।",
            "COMPLEMENTARY"),
        DailyTip(33..36, "Add Protein-Rich Foods", "प्रोटीन वाले खाने जोड़ें",
            "Add well-cooked and mashed dal, khichdi, and egg yolk to baby's diet. Protein is essential for growth and brain development.",
            "बच्चे के आहार में अच्छी तरह पकी और मसली दाल, खिचड़ी और अंडे की जर्दी मिलाएं। प्रोटीन विकास और मस्तिष्क के लिए ज़रूरी है।",
            "COMPLEMENTARY"),
        DailyTip(37..40, "Iron-Rich Foods", "आयरन से भरपूर खाना",
            "Include iron-rich foods: spinach (palak), jaggery (gud), dates, and well-cooked minced meat if non-vegetarian. Iron prevents anaemia and supports brain development.",
            "आयरन वाले खाने शामिल करें: पालक, गुड़, खजूर, और माँसाहारी हों तो अच्छी तरह पका कीमा। आयरन एनीमिया रोकता है और मस्तिष्क विकास में मदद करता है।",
            "COMPLEMENTARY"),
        DailyTip(41..44, "Finger Foods", "उंगलियों से खाने वाले आहार",
            "Offer soft finger foods like small banana pieces, steamed carrot sticks, soft roti pieces. This helps develop pincer grasp and self-feeding skills.",
            "नरम फिंगर फूड दें जैसे छोटे केले के टुकड़े, भाप में पकी गाजर, नरम रोटी के टुकड़े। इससे चुटकी पकड़ और खुद खाने का कौशल विकसित होता है।",
            "COMPLEMENTARY"),
        DailyTip(45..48, "Variety Is Important", "विविधता ज़रूरी है",
            "By now, baby should be eating from multiple food groups daily: grains, vegetables, fruits, protein (dal/egg), and dairy. Continue breastfeeding alongside meals.",
            "अब तक बच्चा रोज़ कई खाद्य समूहों से खाना खाए: अनाज, सब्ज़ियाँ, फल, प्रोटीन (दाल/अंडा), और डेयरी। भोजन के साथ स्तनपान जारी रखें।",
            "COMPLEMENTARY"),
        DailyTip(49..52, "Continue Breastfeeding After 1 Year", "1 साल बाद भी स्तनपान जारी रखें",
            "WHO recommends breastfeeding up to 2 years and beyond. Even after 1 year, breast milk provides important nutrients and antibodies alongside regular meals.",
            "WHO 2 साल और उसके बाद तक स्तनपान की सिफ़ारिश करता है। 1 साल के बाद भी माँ का दूध ज़रूरी पोषक तत्व और एंटीबॉडी देता है।",
            "BREASTFEEDING")
    )

    val mythBusters: List<MythBuster> = listOf(
        MythBuster(
            "Is formula milk better than breast milk?",
            "क्या फॉर्मूला दूध माँ के दूध से बेहतर है?",
            "No. Breast milk is the gold standard for infant nutrition. It contains antibodies, enzymes, and perfectly balanced nutrients that formula cannot replicate. WHO recommends exclusive breastfeeding for the first 6 months.",
            "नहीं। माँ का दूध शिशु पोषण का सर्वोत्तम स्रोत है। इसमें एंटीबॉडी, एंजाइम और संतुलित पोषक तत्व होते हैं जो फॉर्मूला में नहीं होते। WHO पहले 6 महीने केवल स्तनपान की सिफ़ारिश करता है।"
        ),
        MythBuster(
            "My milk is not enough for my baby",
            "मेरा दूध बच्चे के लिए पर्याप्त नहीं है",
            "Most mothers produce exactly as much milk as their baby needs. The more your baby feeds, the more milk you produce. True low milk supply is very rare (less than 5% of mothers).",
            "अधिकतर माँएँ उतना ही दूध बनाती हैं जितना बच्चे को चाहिए। बच्चा जितना ज़्यादा पीता है, उतना ज़्यादा दूध बनता है। सच में दूध कम होना बहुत दुर्लभ है (5% से कम माँओं में)।"
        ),
        MythBuster(
            "Should I give water to my newborn in summer?",
            "क्या गर्मी में नवजात को पानी देना चाहिए?",
            "No. Breast milk is 88% water and provides all the hydration a baby needs, even in hot weather. Giving water before 6 months can fill the baby's stomach and reduce milk intake, leading to malnutrition.",
            "नहीं। माँ का दूध 88% पानी है और गर्मी में भी बच्चे को पूरा पानी देता है। 6 महीने से पहले पानी देने से पेट भर जाता है, दूध कम पीता है, कुपोषण हो सकता है।"
        ),
        MythBuster(
            "Should I stop breastfeeding when I'm sick?",
            "क्या बीमार होने पर स्तनपान बंद करना चाहिए?",
            "No. In most illnesses (cold, fever, flu), you should continue breastfeeding. Your milk will contain antibodies that help protect your baby from the same illness. Consult your doctor about medications.",
            "नहीं। अधिकतर बीमारियों (सर्दी, बुखार, फ्लू) में स्तनपान जारी रखें। आपके दूध में एंटीबॉडी होंगी जो बच्चे को उसी बीमारी से बचाएंगी। दवाओं के बारे में डॉक्टर से पूछें।"
        ),
        MythBuster(
            "Colostrum (first milk) should be discarded",
            "कोलोस्ट्रम (पहला दूध) फेंक देना चाहिए",
            "Absolutely not! Colostrum is liquid gold. It's rich in antibodies, vitamins, and minerals. It protects your newborn from infections and helps pass the first stool (meconium). Never discard it.",
            "बिलकुल नहीं! कोलोस्ट्रम तरल सोना है। इसमें एंटीबॉडी, विटामिन और खनिज भरपूर हैं। यह नवजात को संक्रमण से बचाता है और पहला मल (मेकोनियम) निकालने में मदद करता है।"
        ),
        MythBuster(
            "My baby cries after feeding — is the milk not enough?",
            "बच्चा दूध पीने के बाद रोता है — क्या दूध कम है?",
            "Crying after feeding is usually due to gas or colic, not insufficient milk. Try burping your baby after each feed by holding upright and gently patting the back. If baby has 6+ wet diapers daily, milk supply is adequate.",
            "दूध पीने के बाद रोना आमतौर पर गैस या कोलिक के कारण होता है, दूध कम नहीं। हर बार दूध पिलाने के बाद बच्चे को सीधा पकड़कर पीठ थपथपाएं। अगर दिन में 6+ गीले डायपर हैं तो दूध पर्याप्त है।"
        ),
        MythBuster(
            "Can I breastfeed if I had a C-section?",
            "क्या सी-सेक्शन के बाद स्तनपान कर सकती हूँ?",
            "Yes! You can breastfeed after a C-section. It may take a few hours longer for milk to come in, but skin-to-skin contact and early initiation help. Try the football hold position for comfort.",
            "हाँ! सी-सेक्शन के बाद स्तनपान कर सकती हैं। दूध आने में कुछ घंटे ज़्यादा लग सकते हैं, लेकिन त्वचा-से-त्वचा संपर्क और जल्दी शुरू करना मदद करता है।"
        ),
        MythBuster(
            "When should I start giving solid food?",
            "ठोस आहार कब शुरू करें?",
            "Start complementary (solid) foods at 6 months — not before. Signs of readiness: baby can sit with support, shows interest in food, and has lost the tongue-thrust reflex. Continue breastfeeding alongside solids.",
            "6 महीने में पूरक (ठोस) आहार शुरू करें — पहले नहीं। तैयारी के संकेत: बच्चा सहारे से बैठ सकता है, खाने में रुचि दिखाता है। ठोस आहार के साथ स्तनपान जारी रखें।"
        )
    )

    /**
     * Get the appropriate daily tip based on baby's age in weeks.
     */
    fun getTipForWeek(weekNumber: Int): DailyTip? {
        return dailyTips.find { weekNumber in it.weekRange }
    }

    /**
     * Get all tips for a given category.
     */
    fun getTipsByCategory(category: String): List<DailyTip> {
        return dailyTips.filter { it.category == category }
    }
}

package com.shishusneh.data.constants

/**
 * WHO-aligned developmental milestones for 0–52 weeks.
 * Covers 4 domains: MOTOR, LANGUAGE, SOCIAL, COGNITIVE.
 */
object MilestoneData {

    data class MilestoneTemplate(
        val weekNumber: Int,
        val title: String,
        val titleHi: String,
        val description: String,
        val descriptionHi: String,
        val domain: String
    )

    val milestones: List<MilestoneTemplate> = listOf(
        // === NEWBORN (0–4 Weeks) ===
        MilestoneTemplate(1, "Moves arms and legs", "हाथ और पैर हिलाता है",
            "Baby makes jerky arm and leg movements",
            "बच्चा हाथ-पैर की झटकेदार हरकत करता है", "MOTOR"),
        MilestoneTemplate(1, "Responds to loud sounds", "तेज़ आवाज़ पर प्रतिक्रिया",
            "Startles or blinks at loud noises",
            "तेज़ आवाज़ पर चौंकता या पलकें झपकाता है", "LANGUAGE"),
        MilestoneTemplate(1, "Recognizes mother's voice", "माँ की आवाज़ पहचानता है",
            "Calms when hearing mother's voice",
            "माँ की आवाज़ सुनकर शांत होता है", "SOCIAL"),
        MilestoneTemplate(2, "Briefly lifts head on tummy", "पेट के बल सिर उठाता है",
            "Can briefly lift head during tummy time",
            "पेट के बल थोड़ी देर सिर उठाता है", "MOTOR"),
        MilestoneTemplate(3, "Follows objects with eyes", "आँखों से चीज़ें देखता है",
            "Can briefly follow a moving object with eyes",
            "आँखों से हिलती चीज़ को देख सकता है", "COGNITIVE"),
        MilestoneTemplate(4, "Brings hands to face", "हाथ चेहरे पर लाता है",
            "Can bring hands near face and mouth",
            "हाथों को मुँह और चेहरे तक लाता है", "MOTOR"),
        MilestoneTemplate(4, "Stares at faces", "चेहरे को देखता है",
            "Stares intently at faces, especially mother's",
            "चेहरों को ध्यान से देखता है, ख़ासकर माँ का", "SOCIAL"),

        // === 1–2 MONTHS (5–8 Weeks) ===
        MilestoneTemplate(6, "Holds head at 45°", "सिर 45° तक उठाता है",
            "Can hold head at 45-degree angle on tummy",
            "पेट के बल 45 डिग्री तक सिर उठा सकता है", "MOTOR"),
        MilestoneTemplate(6, "Coos and gurgles", "गुनगुनाता है",
            "Makes vowel-like cooing sounds (aah, ooh)",
            "आह, ऊह जैसी आवाज़ें निकालता है", "LANGUAGE"),
        MilestoneTemplate(7, "Social smile", "सामाजिक मुस्कान",
            "Smiles in response to your smile or voice",
            "आपकी मुस्कान या आवाज़ पर मुस्कुराता है", "SOCIAL"),
        MilestoneTemplate(8, "Follows objects side to side", "दोनों तरफ देखता है",
            "Can track a moving toy from side to side",
            "चलती खिलौने को दोनों तरफ देख सकता है", "COGNITIVE"),

        // === 2–3 MONTHS (9–12 Weeks) ===
        MilestoneTemplate(10, "Lifts head and chest", "सीना उठाता है",
            "Pushes up on arms, lifts head and chest on tummy",
            "हाथों पर ज़ोर देकर सीना और सिर उठाता है", "MOTOR"),
        MilestoneTemplate(10, "Different cries for needs", "अलग ज़रूरतों के लिए अलग रोना",
            "Cries become distinct — hunger vs discomfort",
            "रोने की आवाज़ अलग-अलग — भूख, तकलीफ़", "LANGUAGE"),
        MilestoneTemplate(12, "Opens and closes hands", "हाथ खोलता-बंद करता है",
            "Begins to open/close hands, swipes at objects",
            "हाथ खोलना-बंद करना शुरू, चीज़ों पर हाथ मारता है", "MOTOR"),
        MilestoneTemplate(12, "Begins to babble", "बड़बड़ाना शुरू",
            "Starts making repetitive babbling sounds",
            "दोहराव वाली आवाज़ें निकालना शुरू करता है", "LANGUAGE"),

        // === 3–4 MONTHS (13–16 Weeks) ===
        MilestoneTemplate(14, "Holds head steady", "सिर स्थिर रखता है",
            "Holds head steady without support when upright",
            "सीधा पकड़ने पर बिना सहारे सिर स्थिर रखता है", "MOTOR"),
        MilestoneTemplate(14, "Laughs out loud", "ज़ोर से हँसता है",
            "Laughs out loud for the first time",
            "पहली बार ज़ोर से हँसता है", "SOCIAL"),
        MilestoneTemplate(15, "Reaches for toys", "खिलौने पकड़ता है",
            "Reaches for and tries to grab toys",
            "खिलौनों को पकड़ने की कोशिश करता है", "MOTOR"),
        MilestoneTemplate(16, "Recognizes familiar people", "परिचितों को पहचानता है",
            "Shows recognition of familiar people vs strangers",
            "परिचित और अनजान लोगों में फ़र्क पहचानता है", "COGNITIVE"),
        MilestoneTemplate(16, "Turns to sounds", "आवाज़ की ओर मुड़ता है",
            "Turns head to locate source of sound",
            "आवाज़ का स्रोत खोजने के लिए सिर घुमाता है", "LANGUAGE"),

        // === 4–6 MONTHS (17–26 Weeks) ===
        MilestoneTemplate(18, "Rolls tummy to back", "पेट से पीठ पर पलटता है",
            "Can roll over from stomach to back",
            "पेट से पीठ की ओर पलट सकता है", "MOTOR"),
        MilestoneTemplate(18, "Responds to name", "नाम पर प्रतिक्रिया",
            "Starts responding when name is called",
            "नाम पुकारने पर प्रतिक्रिया करता है", "LANGUAGE"),
        MilestoneTemplate(20, "Explores with mouth", "मुँह से पहचानता है",
            "Brings objects to mouth to explore them",
            "चीज़ों को मुँह में डालकर पहचानता है", "COGNITIVE"),
        MilestoneTemplate(22, "Sits with support", "सहारे से बैठता है",
            "Can sit with some support or propped up",
            "थोड़े सहारे से बैठ सकता है", "MOTOR"),
        MilestoneTemplate(22, "Babbles consonants", "व्यंजन बोलता है",
            "Babbles with consonant sounds (ba, da, ma)",
            "बा, दा, मा जैसी आवाज़ें निकालता है", "LANGUAGE"),
        MilestoneTemplate(24, "Rolls both ways", "दोनों तरफ पलटता है",
            "Rolls from tummy to back and back to tummy",
            "दोनों तरफ पलट सकता है", "MOTOR"),
        MilestoneTemplate(24, "Likes looking in mirror", "शीशे में देखता है",
            "Shows interest in own reflection",
            "शीशे में अपना चेहरा देखना पसंद करता है", "SOCIAL"),
        MilestoneTemplate(26, "Transfers objects between hands", "हाथ बदलता है",
            "Passes a toy from one hand to the other",
            "एक हाथ से दूसरे में खिलौना देता है", "MOTOR"),

        // === 6–9 MONTHS (27–39 Weeks) ===
        MilestoneTemplate(28, "Sits without support", "बिना सहारे बैठता है",
            "Sits steadily without any support",
            "बिना सहारे स्थिर बैठ सकता है", "MOTOR"),
        MilestoneTemplate(28, "Responds to 'no'", "'नहीं' समझता है",
            "Begins to understand 'no'",
            "'नहीं' शब्द समझने लगता है", "LANGUAGE"),
        MilestoneTemplate(30, "Plays peek-a-boo", "छुपम-छुपाई खेलता है",
            "Enjoys playing peek-a-boo",
            "छुपम-छुपाई खेलना पसंद करता है", "SOCIAL"),
        MilestoneTemplate(32, "Crawls", "रेंगता है",
            "Starts crawling — traditional or army crawl",
            "रेंगना शुरू करता है", "MOTOR"),
        MilestoneTemplate(32, "Uses pincer grasp", "चुटकी पकड़ करता है",
            "Picks up small objects between thumb and finger",
            "अंगूठे-उंगली से छोटी चीज़ें उठाता है", "MOTOR"),
        MilestoneTemplate(34, "Says mama/dada", "मामा/दादा कहता है",
            "Uses specific sounds like 'mama' and 'dada'",
            "'मामा'/'दादा' जैसी आवाज़ें निकालता है", "LANGUAGE"),
        MilestoneTemplate(35, "Stranger anxiety", "अजनबियों से डरता है",
            "Shows fear of strangers, clings to caregivers",
            "अजनबियों से डरता है, परिचित लोगों से चिपकता है", "SOCIAL"),
        MilestoneTemplate(36, "Pulls to stand", "खड़ा होता है",
            "Pulls self up to stand using furniture",
            "फर्नीचर पकड़कर खड़ा होता है", "MOTOR"),
        MilestoneTemplate(36, "Object permanence", "वस्तु स्थायित्व",
            "Looks for hidden objects",
            "छुपाई चीज़ों को ढूँढता है", "COGNITIVE"),
        MilestoneTemplate(38, "Points to objects", "इशारा करता है",
            "Points at objects to show interest",
            "चीज़ों की ओर उंगली से इशारा करता है", "LANGUAGE"),

        // === 9–12 MONTHS (39–52 Weeks) ===
        MilestoneTemplate(40, "Cruises along furniture", "फर्नीचर पकड़कर चलता है",
            "Walks while holding onto furniture",
            "फर्नीचर पकड़कर चलता है", "MOTOR"),
        MilestoneTemplate(40, "Waves bye-bye", "बाय-बाय करता है",
            "Learns to wave goodbye",
            "बाय-बाय करना सीखता है", "SOCIAL"),
        MilestoneTemplate(42, "Follows simple instructions", "सरल निर्देश मानता है",
            "Follows simple commands with gestures",
            "इशारों के साथ सरल बातें मानता है", "COGNITIVE"),
        MilestoneTemplate(42, "Says 1-2 words", "1-2 शब्द बोलता है",
            "Says words like mama, dada, no",
            "मामा, दादा, ना जैसे शब्द बोलता है", "LANGUAGE"),
        MilestoneTemplate(46, "Stands alone briefly", "अकेला खड़ा होता है",
            "Stands independently for a few seconds",
            "कुछ सेकंड बिना सहारे खड़ा होता है", "MOTOR"),
        MilestoneTemplate(48, "Uses objects correctly", "चीज़ों का सही उपयोग",
            "Uses cup for drinking, phone to ear",
            "कप से पीना, फ़ोन कान पर लगाना", "COGNITIVE"),
        MilestoneTemplate(50, "Takes first steps", "पहले कदम",
            "May take first independent steps",
            "पहले स्वतंत्र कदम उठा सकता है", "MOTOR"),
        MilestoneTemplate(52, "Walks with hand held", "हाथ पकड़कर चलता है",
            "Walks when one hand is held by adult",
            "एक हाथ पकड़कर चल सकता है", "MOTOR"),
        MilestoneTemplate(52, "Shows affection", "प्यार दिखाता है",
            "Hugs, kisses familiar people",
            "परिचित लोगों को गले लगाता है, प्यार दिखाता है", "SOCIAL"),
        MilestoneTemplate(52, "Understands simple questions", "सरल सवाल समझता है",
            "Responds to 'Where is the ball?'",
            "'गेंद कहाँ है?' जैसे सवालों का जवाब देता है", "COGNITIVE")
    )

    fun getMilestonesForWeek(weekNumber: Int): List<MilestoneTemplate> =
        milestones.filter { it.weekNumber == weekNumber }

    fun getWeeksWithMilestones(): List<Int> =
        milestones.map { it.weekNumber }.distinct().sorted()

    fun getNearestMilestoneWeek(currentWeek: Int): Int {
        val weeks = getWeeksWithMilestones()
        return weeks.minByOrNull { kotlin.math.abs(it - currentWeek) } ?: currentWeek
    }
}

package com.speakup.trainer.domain

import com.speakup.trainer.domain.model.Difficulty
import com.speakup.trainer.domain.model.PracticeMode
import com.speakup.trainer.domain.model.Topic
import com.speakup.trainer.domain.model.TopicCategory

/**
 * Provides a hardcoded list of 110+ IELTS-style topics.
 * Topics cover all categories, difficulties, and modes.
 * In a production app this would come from the database or a remote API.
 */
object TopicProvider {

    val allTopics: List<Topic> = buildList {

        // ─── EDUCATION ─────────────────────────────────────────────────────────
        add(Topic(1, "University Education", "Describe the advantages and disadvantages of attending university. Speak for 1–2 minutes.", TopicCategory.EDUCATION, Difficulty.EASY, PracticeMode.SPEAKING))
        add(Topic(2, "Online Learning", "Do you think online learning is as effective as face-to-face education? Write 250 words.", TopicCategory.EDUCATION, Difficulty.MEDIUM, PracticeMode.WRITING))
        add(Topic(3, "Gap Year", "Should students take a gap year before starting university? Speak for 1–2 minutes.", TopicCategory.EDUCATION, Difficulty.EASY, PracticeMode.SPEAKING))
        add(Topic(4, "Private vs Public Schools", "Compare private and public school education. Write 250 words.", TopicCategory.EDUCATION, Difficulty.MEDIUM, PracticeMode.WRITING))
        add(Topic(5, "Importance of Libraries", "How important are public libraries in the digital age? Speak for 1–2 minutes.", TopicCategory.EDUCATION, Difficulty.EASY, PracticeMode.SPEAKING))
        add(Topic(6, "Tuition Fees", "Should university education be free for all students? Write 250 words.", TopicCategory.EDUCATION, Difficulty.HARD, PracticeMode.WRITING))
        add(Topic(7, "Rote Learning vs Critical Thinking", "Compare rote memorization with critical thinking in education. Speak for 1–2 minutes.", TopicCategory.EDUCATION, Difficulty.HARD, PracticeMode.SPEAKING))
        add(Topic(8, "Literacy Rates", "Why is improving global literacy rates important? Write 250 words.", TopicCategory.EDUCATION, Difficulty.MEDIUM, PracticeMode.WRITING))
        add(Topic(9, "Homeschooling", "What are the pros and cons of homeschooling children? Speak for 1–2 minutes.", TopicCategory.EDUCATION, Difficulty.MEDIUM, PracticeMode.SPEAKING))
        add(Topic(10, "STEM Education", "Is the focus on STEM subjects reducing creativity in schools? Write 250 words.", TopicCategory.EDUCATION, Difficulty.HARD, PracticeMode.WRITING))
        add(Topic(11, "School Uniforms", "Do school uniforms improve the learning environment? Speak for 1–2 minutes.", TopicCategory.EDUCATION, Difficulty.EASY, PracticeMode.SPEAKING))

        // ─── WORK ───────────────────────────────────────────────────────────────
        add(Topic(12, "Work-Life Balance", "How can people achieve a healthy work-life balance? Speak for 1–2 minutes.", TopicCategory.WORK, Difficulty.EASY, PracticeMode.SPEAKING))
        add(Topic(13, "Remote Work", "Is working from home beneficial for employees and companies? Write 250 words.", TopicCategory.WORK, Difficulty.MEDIUM, PracticeMode.WRITING))
        add(Topic(14, "Job Satisfaction", "What factors contribute most to job satisfaction? Speak for 1–2 minutes.", TopicCategory.WORK, Difficulty.EASY, PracticeMode.SPEAKING))
        add(Topic(15, "Gender Pay Gap", "What are the causes and solutions to the gender pay gap? Write 250 words.", TopicCategory.WORK, Difficulty.HARD, PracticeMode.WRITING))
        add(Topic(16, "Automation & Jobs", "Will automation lead to mass unemployment? Speak for 1–2 minutes.", TopicCategory.WORK, Difficulty.HARD, PracticeMode.SPEAKING))
        add(Topic(17, "Minimum Wage", "Should governments raise the minimum wage? Write 250 words.", TopicCategory.WORK, Difficulty.MEDIUM, PracticeMode.WRITING))
        add(Topic(18, "Career Change", "Describe a time when someone you know changed careers. Speak for 1–2 minutes.", TopicCategory.WORK, Difficulty.EASY, PracticeMode.SPEAKING))
        add(Topic(19, "Freelancing", "Is freelancing better than a traditional 9-to-5 job? Write 250 words.", TopicCategory.WORK, Difficulty.MEDIUM, PracticeMode.WRITING))
        add(Topic(20, "Workplace Stress", "What are the main causes of workplace stress and how can it be reduced? Speak for 1–2 minutes.", TopicCategory.WORK, Difficulty.MEDIUM, PracticeMode.SPEAKING))
        add(Topic(21, "Leadership Skills", "What qualities make a great leader? Write 250 words.", TopicCategory.WORK, Difficulty.EASY, PracticeMode.WRITING))
        add(Topic(22, "Four-Day Work Week", "Should all companies adopt a four-day working week? Speak for 1–2 minutes.", TopicCategory.WORK, Difficulty.HARD, PracticeMode.SPEAKING))

        // ─── SOCIAL LIFE ────────────────────────────────────────────────────────
        add(Topic(23, "Social Media Impact", "How has social media changed the way people interact? Speak for 1–2 minutes.", TopicCategory.SOCIAL_LIFE, Difficulty.EASY, PracticeMode.SPEAKING))
        add(Topic(24, "Community Spirit", "Is the sense of community declining in modern cities? Write 250 words.", TopicCategory.SOCIAL_LIFE, Difficulty.MEDIUM, PracticeMode.WRITING))
        add(Topic(25, "Volunteering", "Why is volunteering important for society? Speak for 1–2 minutes.", TopicCategory.SOCIAL_LIFE, Difficulty.EASY, PracticeMode.SPEAKING))
        add(Topic(26, "Aging Population", "How should governments respond to an aging population? Write 250 words.", TopicCategory.SOCIAL_LIFE, Difficulty.HARD, PracticeMode.WRITING))
        add(Topic(27, "Family Structure", "How have family structures changed over the past 50 years? Speak for 1–2 minutes.", TopicCategory.SOCIAL_LIFE, Difficulty.MEDIUM, PracticeMode.SPEAKING))
        add(Topic(28, "Loneliness in Cities", "Why are people in large cities often lonely? Write 250 words.", TopicCategory.SOCIAL_LIFE, Difficulty.MEDIUM, PracticeMode.WRITING))
        add(Topic(29, "Cultural Diversity", "How does cultural diversity benefit a society? Speak for 1–2 minutes.", TopicCategory.SOCIAL_LIFE, Difficulty.EASY, PracticeMode.SPEAKING))
        add(Topic(30, "Social Inequality", "What are the main causes of social inequality and how can they be addressed? Write 250 words.", TopicCategory.SOCIAL_LIFE, Difficulty.HARD, PracticeMode.WRITING))
        add(Topic(31, "The Role of Friendship", "How important is friendship to personal well-being? Speak for 1–2 minutes.", TopicCategory.SOCIAL_LIFE, Difficulty.EASY, PracticeMode.SPEAKING))
        add(Topic(32, "Marriage Trends", "Why are fewer people getting married today compared to previous generations? Write 250 words.", TopicCategory.SOCIAL_LIFE, Difficulty.MEDIUM, PracticeMode.WRITING))

        // ─── TECHNOLOGY ─────────────────────────────────────────────────────────
        add(Topic(33, "Artificial Intelligence", "What are the benefits and risks of artificial intelligence? Speak for 1–2 minutes.", TopicCategory.TECHNOLOGY, Difficulty.MEDIUM, PracticeMode.SPEAKING))
        add(Topic(34, "Social Media Addiction", "Is social media addiction a serious problem? Write 250 words.", TopicCategory.TECHNOLOGY, Difficulty.EASY, PracticeMode.WRITING))
        add(Topic(35, "Privacy & Technology", "Has technology made our lives more or less private? Speak for 1–2 minutes.", TopicCategory.TECHNOLOGY, Difficulty.HARD, PracticeMode.SPEAKING))
        add(Topic(36, "Electric Vehicles", "Will electric vehicles replace petrol cars within 20 years? Write 250 words.", TopicCategory.TECHNOLOGY, Difficulty.MEDIUM, PracticeMode.WRITING))
        add(Topic(37, "Smartphones", "Describe how smartphones have changed daily life. Speak for 1–2 minutes.", TopicCategory.TECHNOLOGY, Difficulty.EASY, PracticeMode.SPEAKING))
        add(Topic(38, "Cybersecurity", "What are the biggest cybersecurity threats facing societies today? Write 250 words.", TopicCategory.TECHNOLOGY, Difficulty.HARD, PracticeMode.WRITING))
        add(Topic(39, "Space Exploration", "Is investing in space exploration worthwhile? Speak for 1–2 minutes.", TopicCategory.TECHNOLOGY, Difficulty.HARD, PracticeMode.SPEAKING))
        add(Topic(40, "Internet of Things", "How is the Internet of Things transforming everyday life? Write 250 words.", TopicCategory.TECHNOLOGY, Difficulty.MEDIUM, PracticeMode.WRITING))
        add(Topic(41, "Gaming Industry", "Has the gaming industry had a positive or negative effect on young people? Speak for 1–2 minutes.", TopicCategory.TECHNOLOGY, Difficulty.EASY, PracticeMode.SPEAKING))
        add(Topic(42, "Big Data", "How are companies using big data and what are the ethical implications? Write 250 words.", TopicCategory.TECHNOLOGY, Difficulty.HARD, PracticeMode.WRITING))
        add(Topic(43, "Robots in Healthcare", "Should robots be used to care for the elderly? Speak for 1–2 minutes.", TopicCategory.TECHNOLOGY, Difficulty.MEDIUM, PracticeMode.SPEAKING))

        // ─── ENVIRONMENT ────────────────────────────────────────────────────────
        add(Topic(44, "Climate Change", "What actions should individuals take to reduce climate change? Speak for 1–2 minutes.", TopicCategory.ENVIRONMENT, Difficulty.EASY, PracticeMode.SPEAKING))
        add(Topic(45, "Plastic Pollution", "How serious is the problem of plastic waste in our oceans? Write 250 words.", TopicCategory.ENVIRONMENT, Difficulty.MEDIUM, PracticeMode.WRITING))
        add(Topic(46, "Renewable Energy", "Should governments invest more in renewable energy? Speak for 1–2 minutes.", TopicCategory.ENVIRONMENT, Difficulty.MEDIUM, PracticeMode.SPEAKING))
        add(Topic(47, "Deforestation", "What are the main causes and effects of deforestation? Write 250 words.", TopicCategory.ENVIRONMENT, Difficulty.HARD, PracticeMode.WRITING))
        add(Topic(48, "Veganism", "Is a vegan diet better for the environment? Speak for 1–2 minutes.", TopicCategory.ENVIRONMENT, Difficulty.EASY, PracticeMode.SPEAKING))
        add(Topic(49, "Carbon Tax", "Should governments implement a carbon tax to reduce emissions? Write 250 words.", TopicCategory.ENVIRONMENT, Difficulty.HARD, PracticeMode.WRITING))
        add(Topic(50, "Wildlife Conservation", "Why is protecting endangered species important? Speak for 1–2 minutes.", TopicCategory.ENVIRONMENT, Difficulty.EASY, PracticeMode.SPEAKING))
        add(Topic(51, "Urban Green Spaces", "Should cities be required to create more green spaces? Write 250 words.", TopicCategory.ENVIRONMENT, Difficulty.MEDIUM, PracticeMode.WRITING))
        add(Topic(52, "Fast Fashion", "How does the fast fashion industry contribute to environmental damage? Speak for 1–2 minutes.", TopicCategory.ENVIRONMENT, Difficulty.MEDIUM, PracticeMode.SPEAKING))
        add(Topic(53, "Nuclear Energy", "Is nuclear energy a viable solution to the climate crisis? Write 250 words.", TopicCategory.ENVIRONMENT, Difficulty.HARD, PracticeMode.WRITING))

        // ─── HEALTH ─────────────────────────────────────────────────────────────
        add(Topic(54, "Mental Health Awareness", "Why is it important to talk openly about mental health? Speak for 1–2 minutes.", TopicCategory.HEALTH, Difficulty.EASY, PracticeMode.SPEAKING))
        add(Topic(55, "Obesity Epidemic", "What are the causes of obesity and how can governments tackle it? Write 250 words.", TopicCategory.HEALTH, Difficulty.MEDIUM, PracticeMode.WRITING))
        add(Topic(56, "Sports & Health", "How does regular exercise improve overall health? Speak for 1–2 minutes.", TopicCategory.HEALTH, Difficulty.EASY, PracticeMode.SPEAKING))
        add(Topic(57, "Universal Healthcare", "Should healthcare be free and universal for all citizens? Write 250 words.", TopicCategory.HEALTH, Difficulty.HARD, PracticeMode.WRITING))
        add(Topic(58, "Junk Food Advertising", "Should junk food advertising be banned? Speak for 1–2 minutes.", TopicCategory.HEALTH, Difficulty.MEDIUM, PracticeMode.SPEAKING))
        add(Topic(59, "Vaccination", "Are there ethical issues surrounding mandatory vaccination? Write 250 words.", TopicCategory.HEALTH, Difficulty.HARD, PracticeMode.WRITING))
        add(Topic(60, "Work and Health", "Can a stressful job lead to serious health problems? Speak for 1–2 minutes.", TopicCategory.HEALTH, Difficulty.EASY, PracticeMode.SPEAKING))
        add(Topic(61, "Sleep Deprivation", "How does lack of sleep affect physical and mental health? Write 250 words.", TopicCategory.HEALTH, Difficulty.MEDIUM, PracticeMode.WRITING))

        // ─── CULTURE ────────────────────────────────────────────────────────────
        add(Topic(62, "Preserving Local Traditions", "How important is it to preserve traditional cultures in a globalized world? Speak for 1–2 minutes.", TopicCategory.CULTURE, Difficulty.MEDIUM, PracticeMode.SPEAKING))
        add(Topic(63, "Globalisation & Culture", "Is globalisation destroying local cultures? Write 250 words.", TopicCategory.CULTURE, Difficulty.HARD, PracticeMode.WRITING))
        add(Topic(64, "Art Funding", "Should governments fund the arts? Speak for 1–2 minutes.", TopicCategory.CULTURE, Difficulty.MEDIUM, PracticeMode.SPEAKING))
        add(Topic(65, "Museums Free Entry", "Should all museums offer free entry? Write 250 words.", TopicCategory.CULTURE, Difficulty.EASY, PracticeMode.WRITING))
        add(Topic(66, "Language Loss", "Is the loss of minority languages a serious problem? Speak for 1–2 minutes.", TopicCategory.CULTURE, Difficulty.HARD, PracticeMode.SPEAKING))
        add(Topic(67, "Pop Culture Influence", "How does pop culture influence young people's values? Write 250 words.", TopicCategory.CULTURE, Difficulty.MEDIUM, PracticeMode.WRITING))
        add(Topic(68, "Immigration & Identity", "How does immigration affect national identity? Speak for 1–2 minutes.", TopicCategory.CULTURE, Difficulty.HARD, PracticeMode.SPEAKING))

        // ─── TRAVEL ─────────────────────────────────────────────────────────────
        add(Topic(69, "Benefits of Travel", "How does travelling broaden a person's perspective? Speak for 1–2 minutes.", TopicCategory.TRAVEL, Difficulty.EASY, PracticeMode.SPEAKING))
        add(Topic(70, "Tourism's Impact", "Does mass tourism do more harm than good? Write 250 words.", TopicCategory.TRAVEL, Difficulty.MEDIUM, PracticeMode.WRITING))
        add(Topic(71, "Budget Travel", "Describe the advantages and disadvantages of budget travelling. Speak for 1–2 minutes.", TopicCategory.TRAVEL, Difficulty.EASY, PracticeMode.SPEAKING))
        add(Topic(72, "Ecotourism", "Is ecotourism a genuine solution to environmental damage caused by tourism? Write 250 words.", TopicCategory.TRAVEL, Difficulty.HARD, PracticeMode.WRITING))
        add(Topic(73, "Solo Travel", "What are the benefits and risks of travelling alone? Speak for 1–2 minutes.", TopicCategory.TRAVEL, Difficulty.EASY, PracticeMode.SPEAKING))
        add(Topic(74, "Air Travel & Environment", "Should governments tax air travel more heavily to protect the environment? Write 250 words.", TopicCategory.TRAVEL, Difficulty.HARD, PracticeMode.WRITING))
        add(Topic(75, "Favourite Destination", "Describe your favourite travel destination and why you love it. Speak for 1–2 minutes.", TopicCategory.TRAVEL, Difficulty.EASY, PracticeMode.SPEAKING))

        // ─── SCIENCE ────────────────────────────────────────────────────────────
        add(Topic(76, "Genetic Engineering", "What are the ethical issues surrounding genetic engineering in humans? Write 250 words.", TopicCategory.SCIENCE, Difficulty.HARD, PracticeMode.WRITING))
        add(Topic(77, "Animal Testing", "Is animal testing necessary for scientific progress? Speak for 1–2 minutes.", TopicCategory.SCIENCE, Difficulty.MEDIUM, PracticeMode.SPEAKING))
        add(Topic(78, "Cloning", "Should human cloning be allowed for medical research? Write 250 words.", TopicCategory.SCIENCE, Difficulty.HARD, PracticeMode.WRITING))
        add(Topic(79, "Scientific Research Funding", "How should scientific research be prioritised and funded? Speak for 1–2 minutes.", TopicCategory.SCIENCE, Difficulty.MEDIUM, PracticeMode.SPEAKING))
        add(Topic(80, "Artificial Organs", "How will artificial organs change medicine? Write 250 words.", TopicCategory.SCIENCE, Difficulty.MEDIUM, PracticeMode.WRITING))
        add(Topic(81, "Science in Schools", "Should science be made compulsory throughout all school years? Speak for 1–2 minutes.", TopicCategory.SCIENCE, Difficulty.EASY, PracticeMode.SPEAKING))
        add(Topic(82, "Nanotechnology", "What are the potential benefits and risks of nanotechnology? Write 250 words.", TopicCategory.SCIENCE, Difficulty.HARD, PracticeMode.WRITING))

        // ─── ECONOMY ────────────────────────────────────────────────────────────
        add(Topic(83, "Globalisation's Effect on Economy", "Has globalisation made the world economy more or less stable? Speak for 1–2 minutes.", TopicCategory.ECONOMY, Difficulty.HARD, PracticeMode.SPEAKING))
        add(Topic(84, "Income Inequality", "Is the gap between rich and poor widening? Write 250 words.", TopicCategory.ECONOMY, Difficulty.MEDIUM, PracticeMode.WRITING))
        add(Topic(85, "Cryptocurrency", "Will cryptocurrency replace traditional currencies? Speak for 1–2 minutes.", TopicCategory.ECONOMY, Difficulty.HARD, PracticeMode.SPEAKING))
        add(Topic(86, "Consumer Culture", "Has consumer culture made people happier or more materialistic? Write 250 words.", TopicCategory.ECONOMY, Difficulty.MEDIUM, PracticeMode.WRITING))
        add(Topic(87, "Entrepreneurship", "Why is entrepreneurship important for economic growth? Speak for 1–2 minutes.", TopicCategory.ECONOMY, Difficulty.EASY, PracticeMode.SPEAKING))
        add(Topic(88, "International Trade", "Are trade agreements between countries generally beneficial? Write 250 words.", TopicCategory.ECONOMY, Difficulty.HARD, PracticeMode.WRITING))
        add(Topic(89, "Small Businesses", "How can governments better support small businesses? Speak for 1–2 minutes.", TopicCategory.ECONOMY, Difficulty.MEDIUM, PracticeMode.SPEAKING))
        add(Topic(90, "Universal Basic Income", "Should all citizens receive a universal basic income? Write 250 words.", TopicCategory.ECONOMY, Difficulty.HARD, PracticeMode.WRITING))

        // ─── EXTRA MIXED TOPICS ─────────────────────────────────────────────────
        add(Topic(91, "Public Transport", "How can governments encourage people to use public transport? Speak for 1–2 minutes.", TopicCategory.ENVIRONMENT, Difficulty.EASY, PracticeMode.SPEAKING))
        add(Topic(92, "Celebrity Culture", "Has celebrity culture had a negative effect on society? Write 250 words.", TopicCategory.SOCIAL_LIFE, Difficulty.MEDIUM, PracticeMode.WRITING))
        add(Topic(93, "Sport & National Identity", "Can sport strengthen national identity? Speak for 1–2 minutes.", TopicCategory.CULTURE, Difficulty.EASY, PracticeMode.SPEAKING))
        add(Topic(94, "Fake News", "How dangerous is the spread of fake news? Write 250 words.", TopicCategory.TECHNOLOGY, Difficulty.MEDIUM, PracticeMode.WRITING))
        add(Topic(95, "Children & Technology", "Are children spending too much time on technology? Speak for 1–2 minutes.", TopicCategory.TECHNOLOGY, Difficulty.EASY, PracticeMode.SPEAKING))
        add(Topic(96, "Higher Taxes for the Wealthy", "Should the wealthiest citizens pay higher taxes? Write 250 words.", TopicCategory.ECONOMY, Difficulty.HARD, PracticeMode.WRITING))
        add(Topic(97, "Urban vs Rural Living", "Is urban life better than rural life? Speak for 1–2 minutes.", TopicCategory.SOCIAL_LIFE, Difficulty.EASY, PracticeMode.SPEAKING))
        add(Topic(98, "Food Security", "How can the world ensure food security for a growing population? Write 250 words.", TopicCategory.ENVIRONMENT, Difficulty.HARD, PracticeMode.WRITING))
        add(Topic(99, "Charitable Giving", "Should charity giving be encouraged by tax incentives? Speak for 1–2 minutes.", TopicCategory.ECONOMY, Difficulty.MEDIUM, PracticeMode.SPEAKING))
        add(Topic(100, "Digital Libraries", "Will digital libraries make physical ones obsolete? Write 250 words.", TopicCategory.TECHNOLOGY, Difficulty.MEDIUM, PracticeMode.WRITING))
        add(Topic(101, "Bilingual Education", "What are the cognitive benefits of bilingual education? Speak for 1–2 minutes.", TopicCategory.EDUCATION, Difficulty.MEDIUM, PracticeMode.SPEAKING))
        add(Topic(102, "Drones in Society", "How are drones being used and what regulations are needed? Write 250 words.", TopicCategory.TECHNOLOGY, Difficulty.MEDIUM, PracticeMode.WRITING))
        add(Topic(103, "Public Speaking Skills", "Why is public speaking an essential life skill? Speak for 1–2 minutes.", TopicCategory.EDUCATION, Difficulty.EASY, PracticeMode.SPEAKING))
        add(Topic(104, "Water Scarcity", "What solutions exist for the global water scarcity problem? Write 250 words.", TopicCategory.ENVIRONMENT, Difficulty.HARD, PracticeMode.WRITING))
        add(Topic(105, "Meditation & Wellbeing", "How can meditation improve mental health and productivity? Speak for 1–2 minutes.", TopicCategory.HEALTH, Difficulty.EASY, PracticeMode.SPEAKING))
        add(Topic(106, "Prison Reform", "Should the purpose of prison be punishment or rehabilitation? Write 250 words.", TopicCategory.SOCIAL_LIFE, Difficulty.HARD, PracticeMode.WRITING))
        add(Topic(107, "Economic Migration", "What are the economic effects of migration on host countries? Speak for 1–2 minutes.", TopicCategory.ECONOMY, Difficulty.HARD, PracticeMode.SPEAKING))
        add(Topic(108, "Social Media & Politics", "Is social media making political discourse more extreme? Write 250 words.", TopicCategory.TECHNOLOGY, Difficulty.HARD, PracticeMode.WRITING))
        add(Topic(109, "Parenting Styles", "Which parenting style is most effective for raising confident children? Speak for 1–2 minutes.", TopicCategory.SOCIAL_LIFE, Difficulty.MEDIUM, PracticeMode.SPEAKING))
        add(Topic(110, "Intellectual Property", "How should intellectual property rights be enforced in the digital age? Write 250 words.", TopicCategory.TECHNOLOGY, Difficulty.HARD, PracticeMode.WRITING))
    }

    /**
     * Filter topics by the given parameters. If a parameter is null, it is not applied as a filter.
     */
    fun getFiltered(
        mode: PracticeMode? = null,
        difficulty: Difficulty? = null,
        category: TopicCategory? = null
    ): List<Topic> {
        return allTopics.filter { topic ->
            (mode == null || topic.mode == mode) &&
            (difficulty == null || topic.difficulty == difficulty) &&
            (category == null || topic.category == category)
        }
    }

    /**
     * Returns a random topic matching the given filters.
     * Falls back to any topic if no matches are found.
     */
    fun getRandom(
        mode: PracticeMode? = null,
        difficulty: Difficulty? = null,
        category: TopicCategory? = null
    ): Topic {
        val pool = getFiltered(mode, difficulty, category)
        return if (pool.isNotEmpty()) pool.random() else allTopics.random()
    }
}

package app.krafted.jokersjuggle.viewmodel

import app.krafted.jokersjuggle.game.JokerEvent

private val ACT_END_LINES = listOf(
    "Act complete. Barely.",
    "You survived. The crowd is forgiving."
)

val JOKER_LINES: Map<JokerEvent, List<String>> = mapOf(
    JokerEvent.ACT_START to listOf(
        "The curtain rises.",
        "Do not disappoint.",
        "Try not to embarrass me."
    ),
    JokerEvent.COMBO_5 to listOf(
        "Not bad. For a beginner.",
        "Mildly competent."
    ),
    JokerEvent.COMBO_10 to listOf(
        "You're actually good. Irritating.",
        "Hmph. The crowd is awake."
    ),
    JokerEvent.COMBO_20 to listOf(
        "The audience LOVES you.",
        "I am... conflicted."
    ),
    JokerEvent.DROP to listOf(
        "It hit the floor. They gasped.",
        "Tragic. Truly tragic.",
        "Pick it up. Oh wait — you can't."
    ),
    JokerEvent.LUCKY_7 to listOf(
        "SEVEN! The crowd goes WILD!",
        "Lucky. Don't get used to it."
    ),
    JokerEvent.GOLD_X to listOf(
        "HA! Wrong one!",
        "Was that on purpose?",
        "Even I didn't see that coming."
    ),
    JokerEvent.LIFE_LOST to listOf(
        "One gone. Focus.",
        "Two left. Concentrate."
    ),
    JokerEvent.LAST_LIFE to listOf(
        "ONE LIFE. No pressure.",
        "Everything on the line now."
    ),
    JokerEvent.ACT_COMPLETE to ACT_END_LINES,
    JokerEvent.GAME_OVER to ACT_END_LINES,
    JokerEvent.CHAOS_WIND to listOf(
        "A sudden wind. How unfortunate.",
        "Hope you packed an umbrella."
    ),
    JokerEvent.CHAOS_RUSH to listOf(
        "FASTER! FASTER!",
        "I CAN'T STOP LAUGHING"
    ),
    JokerEvent.CHAOS_DARK to listOf(
        "Can you juggle in the dark?",
        "Lights out, darling."
    ),
    JokerEvent.CHAOS_MIRROR to listOf(
        "Left is right. Right is wrong.",
        "Switch hands. Or don't."
    )
)

private val EVENT_MOODS: Map<JokerEvent, JokerExpression> = mapOf(
    JokerEvent.ACT_START to JokerExpression.THEATRICAL,
    JokerEvent.COMBO_5 to JokerExpression.AMUSED,
    JokerEvent.COMBO_10 to JokerExpression.IMPRESSED,
    JokerEvent.COMBO_20 to JokerExpression.GLEEFUL,
    JokerEvent.DROP to JokerExpression.LAUGHING,
    JokerEvent.LUCKY_7 to JokerExpression.ECSTATIC,
    JokerEvent.GOLD_X to JokerExpression.LAUGHING,
    JokerEvent.LIFE_LOST to JokerExpression.SINISTER,
    JokerEvent.LAST_LIFE to JokerExpression.UNHINGED,
    JokerEvent.ACT_COMPLETE to JokerExpression.THEATRICAL,
    JokerEvent.GAME_OVER to JokerExpression.TRIUMPHANT,
    JokerEvent.CHAOS_WIND to JokerExpression.SINISTER,
    JokerEvent.CHAOS_RUSH to JokerExpression.UNHINGED,
    JokerEvent.CHAOS_DARK to JokerExpression.SINISTER,
    JokerEvent.CHAOS_MIRROR to JokerExpression.LAUGHING
)

fun quoteFor(e: JokerEvent): Pair<JokerExpression, String> {
    val expression = EVENT_MOODS[e] ?: JokerExpression.THEATRICAL
    val quote = JOKER_LINES[e]?.randomOrNull() ?: ""
    return expression to quote
}

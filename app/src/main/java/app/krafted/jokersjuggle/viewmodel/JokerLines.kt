package app.krafted.jokersjuggle.viewmodel

import app.krafted.jokersjuggle.game.JokerEvent

val JOKER_LINES: Map<JokerEvent, List<String>> = mapOf(
    JokerEvent.GAME_START to listOf("The performance begins. Two objects. Modest start."),
    JokerEvent.OBJECT_ADDED_3 to listOf("Three now. The crowd leans forward."),
    JokerEvent.OBJECT_ADDED_5 to listOf("Five in the air. The audience is holding its breath."),
    JokerEvent.OBJECT_ADDED_7 to listOf("SEVEN! All seven! I have never seen this before!"),
    JokerEvent.DROP to listOf("One falls. The crowd gasps. You feel it?"),
    JokerEvent.GOLD_X_CAUGHT to listOf("You caught the wrong one! Magnificent mistake!"),
    JokerEvent.GOLD_X_DROPPED to listOf("Let it fall. You're learning."),
    JokerEvent.LUCKY_7_CAUGHT to listOf("SEVEN! Two hundred points! The house goes wild!"),
    JokerEvent.JOKER_HAT_CAUGHT to listOf("Another one joins! Keep them all up!"),
    JokerEvent.STREAK_5_OBJ_30S to listOf("Thirty seconds. Five objects. You have real skill."),
    JokerEvent.LAST_LIFE to listOf("ONE LIFE. Every object matters now. No exceptions."),
    JokerEvent.NEW_HIGH_SCORE to listOf("A new record. You surprised me. That is rare."),
    JokerEvent.GAME_OVER_LOW to listOf("Two objects. That is where it ended. Noted."),
    JokerEvent.GAME_OVER_HIGH to listOf("Five objects before the fall. Respectable. Genuinely."),
    JokerEvent.CHAOS_WIND to listOf("A sudden wind... let's see how you adjust."),
    JokerEvent.CHAOS_RUSH to listOf("Gravity has opinions today. Different ones."),
    JokerEvent.CHAOS_DARK to listOf("Can you juggle what you cannot see?"),
    JokerEvent.CHAOS_MIRROR to listOf("Left is right. Right is left. Simple."),
    JokerEvent.COMBO_5 to listOf("Not bad. For a beginner."),
    JokerEvent.COMBO_10 to listOf("You're actually good. Irritating."),
    JokerEvent.COMBO_20 to listOf("The audience LOVES you."),
    JokerEvent.LIFE_LOST to listOf("One gone. Focus.")
)

private val EVENT_MOODS: Map<JokerEvent, JokerExpression> = mapOf(
    JokerEvent.GAME_START to JokerExpression.THEATRICAL,
    JokerEvent.OBJECT_ADDED_3 to JokerExpression.AMUSED,
    JokerEvent.OBJECT_ADDED_5 to JokerExpression.IMPRESSED,
    JokerEvent.OBJECT_ADDED_7 to JokerExpression.ECSTATIC,
    JokerEvent.DROP to JokerExpression.LAUGHING,
    JokerEvent.GOLD_X_CAUGHT to JokerExpression.LAUGHING,
    JokerEvent.GOLD_X_DROPPED to JokerExpression.AMUSED,
    JokerEvent.LUCKY_7_CAUGHT to JokerExpression.ECSTATIC,
    JokerEvent.JOKER_HAT_CAUGHT to JokerExpression.LAUGHING,
    JokerEvent.STREAK_5_OBJ_30S to JokerExpression.GENUINE,
    JokerEvent.LAST_LIFE to JokerExpression.UNHINGED,
    JokerEvent.NEW_HIGH_SCORE to JokerExpression.SURPRISED,
    JokerEvent.GAME_OVER_LOW to JokerExpression.TAUNTING,
    JokerEvent.GAME_OVER_HIGH to JokerExpression.RESPECTFUL,
    JokerEvent.CHAOS_WIND to JokerExpression.SINISTER,
    JokerEvent.CHAOS_RUSH to JokerExpression.UNHINGED,
    JokerEvent.CHAOS_DARK to JokerExpression.SINISTER,
    JokerEvent.CHAOS_MIRROR to JokerExpression.LAUGHING,
    JokerEvent.COMBO_5 to JokerExpression.AMUSED,
    JokerEvent.COMBO_10 to JokerExpression.IMPRESSED,
    JokerEvent.COMBO_20 to JokerExpression.GLEEFUL,
    JokerEvent.LIFE_LOST to JokerExpression.SINISTER
)

fun quoteFor(e: JokerEvent): Pair<JokerExpression, String> {
    val expression = EVENT_MOODS[e] ?: JokerExpression.NEUTRAL
    val quote = JOKER_LINES[e]?.randomOrNull() ?: ""
    return expression to quote
}

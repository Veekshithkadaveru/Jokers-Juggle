package app.krafted.jokersjuggle.game

class ComboTracker {
    var comboStreak: Int = 0
        private set
    var comboPeak: Int = 0
        private set

    fun incrementStreak() {
        comboStreak++
        if (comboStreak > comboPeak) {
            comboPeak = comboStreak
        }
    }

    fun resetStreak() {
        comboStreak = 0
    }

    fun getMultiplier(): Int {
        return when {
            comboStreak >= 20 -> 5
            comboStreak >= 15 -> 4
            comboStreak >= 10 -> 3
            comboStreak >= 5 -> 2
            else -> 1
        }
    }
}

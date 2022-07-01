package com.dicoding.todoapp.ui.list

import android.app.Activity
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage
import com.dicoding.todoapp.R
import com.dicoding.todoapp.ui.add.AddTaskActivity
import org.junit.Assert
import org.junit.Before
import org.junit.Test

//TODO 16 : Write UI test to validate when user tap Add Task (+), the AddTaskActivity displayed
class TaskActivityTest {
    @Before
    fun setup(){
        ActivityScenario.launch(TaskActivity::class.java)
    }

    @Test
    fun testAddHabit() {
        lateinit var currActivity: Activity
        Espresso.onView(withId(R.id.fab)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(withId(R.id.fab)).perform(ViewActions.click())

        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            run {
                currActivity = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(
                    Stage.RESUMED).elementAtOrNull(0)!!
            }
        }

        Assert.assertTrue(currActivity.javaClass == AddTaskActivity::class.java)
    }
}
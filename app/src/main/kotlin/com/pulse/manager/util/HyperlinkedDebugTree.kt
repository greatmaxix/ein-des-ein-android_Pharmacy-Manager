package com.pulse.manager.util

import timber.log.Timber

class HyperlinkedDebugTree : Timber.DebugTree() {

    override fun createStackElementTag(element: StackTraceElement) = element.run { "($fileName:$lineNumber)$methodName()" }
}
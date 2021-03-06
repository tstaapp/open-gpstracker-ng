/*------------------------------------------------------------------------------
 **     Ident: Sogeti Smart Mobile Solutions
 **    Author: rene
 ** Copyright: (c) 2017 Sogeti Nederland B.V. All Rights Reserved.
 **------------------------------------------------------------------------------
 ** Sogeti Nederland B.V.            |  No part of this file may be reproduced
 ** Distributed Software Engineering |  or transmitted in any form or by any
 ** Lange Dreef 17                   |  means, electronic or mechanical, for the
 ** 4131 NJ Vianen                   |  purpose, without the express written
 ** The Netherlands                  |  permission of the copyright holder.
 *------------------------------------------------------------------------------
 *
 *   This file is part of OpenGPSTracker.
 *
 *   OpenGPSTracker is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   OpenGPSTracker is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with OpenGPSTracker.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package nl.sogeti.android.gpstracker.ng.wear

import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.IBinder
import nl.sogeti.android.gpstracker.ng.common.GpsTrackerApplication
import javax.inject.Inject


class LoggingService : Service() {

    @Inject
    lateinit var statisticsCollector: StatisticsCollector

    init {
        GpsTrackerApplication.appComponent.inject(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val trackUri: Uri? = intent?.getParcelableExtra(TRACK)
        val state = intent?.getStringExtra(STATE)
        if (trackUri != null) {
            statisticsCollector.start(this, trackUri)
        } else if (state != null) {
            when (state) {
                PAUSE -> statisticsCollector.pause()
                STOP -> stopSelf()
            }

        }

        return START_REDELIVER_INTENT
    }

    override fun onDestroy() {
        statisticsCollector.stop()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {

        private const val TRACK = "WearLoggingService_Track"
        private const val STATE = "WearLoggingService_State"
        private const val PAUSE = "WearLoggingService_State_Paused"
        private const val STOP = "WearLoggingService_State_Stopped"

        @JvmStatic
        fun createStartedIntent(context: Context, trackUri: Uri): Intent {
            val intent = Intent(context, LoggingService::class.java)
            intent.putExtra(TRACK, trackUri)
            return intent
        }

        @JvmStatic
        fun createStopIntent(context: Context): Intent {
            return Intent(context, LoggingService::class.java)
        }

        fun createPausedIntent(context: Context): Intent {
            val intent = Intent(context, LoggingService::class.java)
            intent.putExtra(STATE, PAUSE)
            return intent
        }

        fun createStoppedIntent(context: Context): Intent {
            val intent = Intent(context, LoggingService::class.java)
            intent.putExtra(STATE, STOP)
            return intent
        }
    }
}

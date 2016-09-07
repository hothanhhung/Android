package com.hth.utils;

import android.text.Html;

public class ScheduleItem {

	public int ScheduleId;
	public String ChannelKey;
	public String DateOn;
	public String StartOn;
	public String ProgramName;
	public String Note;

	public String getChannelKey() {
		return ChannelKey;
	}

	public void setChannelKey(String channelKey) {
		ChannelKey = channelKey;
	}

	public String getDateOn() {
		return DateOn;
	}

	public void setDateOn(String dateOn) {
		DateOn = dateOn;
	}

	public String getNote() {
		return Note;
	}

	public void setNote(String note) {
		Note = note;
	}

	public String getProgramName() {
		return ProgramName;
	}

	public void setProgramName(String programName) {
		ProgramName = programName;
	}

	public String getTextProgramName() {

		return Html.fromHtml(ProgramName).toString();
	}

	public int getScheduleId() {
		return ScheduleId;
	}

	public void setScheduleId(int scheduleId) {
		ScheduleId = scheduleId;
	}

	public String getStartOn() {
		return StartOn;
	}

	public void setStartOn(String startOn) {
		StartOn = startOn;
	}

	public String getKey()
	{
		return getDateOn().concat(getStartOn()).concat(getChannelKey());
	}
}

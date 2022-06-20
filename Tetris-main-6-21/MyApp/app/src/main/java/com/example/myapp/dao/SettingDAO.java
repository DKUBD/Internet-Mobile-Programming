package com.example.myapp.dao;


import android.content.Context;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.example.myapp.dto.Setting;
import com.example.myapp.ults.DAOHelper;

public class SettingDAO {
    public static SettingDAO instance;
    private XmlPullParser parserSetting;

    public static SettingDAO getInstance() {
        if (instance == null) {
            instance = new SettingDAO();
        }
        return instance;
    }

    public Setting getSetting(Context context) {
        Setting setting = null;
        BufferedReader bufferedReader;
        try {
            String fileName = context.getFilesDir() + "/" + "database.xml";
            bufferedReader = new BufferedReader(new FileReader(fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            bufferedReader = null;
        }

        if (bufferedReader != null) {
            //prepare parser to parse database.xml
            try {
                parserSetting = Xml.newPullParser();
                parserSetting.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parserSetting.setInput(bufferedReader);
                parserSetting.nextTag();
            } catch (XmlPullParserException | IOException e) {
                e.printStackTrace();
                parserSetting = null;
            }

            if (parserSetting != null) {
                //parsing process
                try {
                    //read Database tag (root tag) in database
                    parserSetting.require(XmlPullParser.START_TAG, null, "Database");

                    //start reading
                    while (parserSetting.next() != XmlPullParser.END_TAG) {
                        if (parserSetting.getEventType() == XmlPullParser.START_TAG) {
                            //start by looking for Setting tag
                            if (parserSetting.getName().equals("Setting")) {
                                //read Setting tag
                                setting = readSettingTag();
                            } else {
                                //skip other tag
                                DAOHelper.skipParser(parserSetting);
                            }
                        }
                    }
                } catch (XmlPullParserException | IOException e) {
                    e.printStackTrace();
                }
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return setting;
    }

    Setting readSettingTag() {
        Setting setting = null;
        try {
            //variables to store Setting's data
            long scoreLimit = -1, lineScore = -1;
            String controlScheme = null;
            ArrayList<ArrayList<Integer>> timeLevels = null;
            int width = -1, height = -1;

            //start reading Setting tag
            parserSetting.require(XmlPullParser.START_TAG, null, "Setting");
            while (parserSetting.next() != XmlPullParser.END_TAG) {
                if (parserSetting.getEventType() == XmlPullParser.START_TAG) {
                    String childName = parserSetting.getName();
                    switch (childName) {
                        case "ScoreLimit": {
                            parserSetting.require(XmlPullParser.START_TAG, null, "ScoreLimit");
                            scoreLimit = Long.parseLong(parserSetting.getAttributeValue(null, "value"));
                            parserSetting.next();
                            parserSetting.require(XmlPullParser.END_TAG, null, "ScoreLimit");
                            break;
                        }
                        case "LineScore": {
                            parserSetting.require(XmlPullParser.START_TAG, null, "LineScore");
                            lineScore = Long.parseLong(parserSetting.getAttributeValue(null, "value"));
                            parserSetting.next();
                            parserSetting.require(XmlPullParser.END_TAG, null, "LineScore");
                            break;
                        }
                        case "TimeLevel": {
                            parserSetting.require(XmlPullParser.START_TAG, null, "TimeLevel");
                            timeLevels = readTimeLevel();
                            parserSetting.require(XmlPullParser.END_TAG, null, "TimeLevel");
                            break;
                        }
                        case "ControlScheme": {
                            parserSetting.require(XmlPullParser.START_TAG, null, "ControlScheme");
                            controlScheme = parserSetting.getAttributeValue(null, "value");
                            parserSetting.next();
                            parserSetting.require(XmlPullParser.END_TAG, null, "ControlScheme");
                            break;
                        }
                        case "BoardSize": {
                            parserSetting.require(XmlPullParser.START_TAG, null, "BoardSize");
//                            width = Integer.parseInt(parserSetting.getAttributeValue(null, "width"));
//                            height = Integer.parseInt(parserSetting.getAttributeValue(null, "height"));

                            // temp. original value: width == 6, height == 30.
                            width = 16;
                            height = 30;
                            parserSetting.next();
                            parserSetting.require(XmlPullParser.END_TAG, null, "BoardSize");
                            break;
                        }
                    }
                }
            }
            System.out.println(scoreLimit);
            System.out.println(lineScore);
            System.out.println(controlScheme);
            System.out.println(timeLevels);
            System.out.println(width);
            System.out.println(height);

            if (scoreLimit != -1 && lineScore != -1 && controlScheme != null && timeLevels != null
                    && width != -1 && height != -1) {
                setting = new Setting(lineScore, timeLevels, width, height);
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }

        return setting;
    }

    private ArrayList<ArrayList<Integer>> readTimeLevel() {
        ArrayList<ArrayList<Integer>> timeLevel = new ArrayList<>();
        try {
            while (parserSetting.next() != XmlPullParser.END_TAG) {
                if (parserSetting.getEventType() == XmlPullParser.START_TAG) {
                    ArrayList<Integer> level = new ArrayList<>();
                    level.add(Integer.parseInt(parserSetting.getAttributeValue(null, "timestamp")));
                    level.add(Integer.parseInt(parserSetting.getAttributeValue(null, "dropspeed")));
                    timeLevel.add(level);
                    parserSetting.next();
                }
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
            timeLevel = null;
        }
        return timeLevel;
    }

}
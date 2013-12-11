/***************************************************************************
 * 5724-H93
 * (c) Copyright Rocket Software, Inc. 2002 - 2009 All Rights Reserved.
 *
 * $Id: Messages.java,v 1.7 2013/09/12 06:16:00 dgorkove Exp $
 ****************************************************************************/

package com.rs.utils;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;


/**
 * This class is used for internationalization messages of web service.
 * 
 * @author amarkelov
 */
public class Messages
{
    /**
     * This field defines location of web services resources.
     */
    private static final String BUNDLE_NAME = "com/rs/utils/root";

    /**
     * This field specifies locale.
     */
    private static ResourceBundle resourceBundle;

    static
    {
        	resourceBundle = ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault());
    }

    /**
     * Return message in en_US locale if message was found by the key.
     */
    public static String getDefaultMessage(String key, Object... args)
    {
        if (resourceBundle != null)
        {
            resourceBundle = ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault());
        }

        return MessageFormat.format(getString(key), args);
    }

    /**
     * Return message in en_US locale if message was found by the key.
     */
    public static String getMessage(String key)
    {
        resourceBundle = ResourceBundle.getBundle(
            BUNDLE_NAME, new Locale("en", "US"));

        return getString(key);
    }

    /**
     * Localize message in setting locale.
     */
    public static String getMessage(String key, Locale locale)
    {
        if (locale != null)
        {
            resourceBundle = ResourceBundle.getBundle(BUNDLE_NAME, locale);
        }
        else
        {
            resourceBundle = ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault());
        }

        return getString(key);
    }

    /**
     * Localize message in setting locale.
     */
    public static String getMessage(String key, Locale locale, Object[] args)
    {
        if (locale != null)
        {
            resourceBundle = ResourceBundle.getBundle(BUNDLE_NAME, locale);
        }
        else
        {
            resourceBundle = ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault());
        }

        return getMessage(key, args);
    }

    /**
     * Return formatting message in en_US locale.
     */
    public static String getMessage(String key, Object[] args)
    {
        return MessageFormat.format(getString(key), args);
    }
    
    /**
     * This method return localized string if the message was found
     *   in resource file otherwise return quoted key.
     * 
     * @param key - key for finding message.
     * 
     * @return localized string if the message was found
     *   in resource file otherwise return quoted key.
     */
    private static String getString(String key)
    {
        try
        {
            String locStr = resourceBundle.getString(key);
            if (isKeyErrorCode(key))
            {
                locStr = (key  + ": " + locStr);
            }
            return locStr;
        }
        catch (NullPointerException nex)
        {
            return "!null!";
        }
        catch (MissingResourceException e)
        {
            return '!' + key + '!';
        }
    }

    /**
     * This method checks whether the <code>message</code> ARY code.
     *   ARY code has view: ARYXXXX{W|I|E}.
     */
    private static boolean isKeyErrorCode(String message)
    {
        boolean result = false;
        if (message.startsWith("ARY") && message.length() == 8)
        {
            String number = message.substring(3, 7);
            try
            {
                Integer.parseInt(number);
                result = true;
            }
            catch (NumberFormatException nfex)
            {
                //--> ignore
            }
        }

        return result;
    }

    /**
     * Prohibit creating objects of type <code>WSMessages</code>.
     */
    private Messages() {}
}

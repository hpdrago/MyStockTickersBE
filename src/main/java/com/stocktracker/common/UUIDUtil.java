package com.stocktracker.common;

import java.util.regex.Pattern;

/**
 * Extension of fasterxml's UUIDUtil
 */
public class UUIDUtil extends com.fasterxml.uuid.impl.UUIDUtil
{
    private static final String uuidRegEx = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}";
    private static final Pattern uuidPattern = Pattern.compile( uuidRegEx );

    /**
     * Determines if the uuid string value matches the UUID regex.
     * @param uuid
     * @return
     */
    public static boolean isUUID( final String uuid )
    {
        return uuidPattern.matcher( uuid )
                          .matches();
    }
}

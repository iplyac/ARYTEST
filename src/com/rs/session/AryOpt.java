/***************************************************************************
 * 5724-H93
 * (c) Copyright Rocket Software, Inc. 2002 - 2009 All Rights Reserved.
 *
 * $Id: AryOpt.java,v 1.2 2011/03/02 10:11:51 amarkelov Exp $
 ****************************************************************************/

package com.rs.session;

/**
 * This class contains information about one ARY option.
 * 
 * @author amarkelov
 */
public class AryOpt
{
    /*
     * Option types.
     */
    public static final int WITH_VALUE = 0;
    public static final int WITHOUT_VALUE = 1;

    /**
     * Option name.
     */
    private String key;

    /**
     * Partition for option.
     */
    private int partition;

    /**
     * Option value.
     */
    private String value;

    /**
     * Option type.
     */
    private int type;

    /**
     * Default constructor.
     */
    public AryOpt() {}

    /**
     * Constructor.
     */
    public AryOpt(
        String key,
        String value,
        int partition,
        int type)
    {
        this.key = key;
        this.value = value;
        this.partition = partition;
        this.type = type;
    }

    public String getKey()
    {
        return key;
    }

    public int getPartition()
    {
        return partition;
    }

    public String getValue()
    {
        return value;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    public void setPartition(int partition)
    {
        this.partition = partition;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    @Override
    public String toString()
    {
        return ("AryOpt[key=" + key + ", value=" + value + ", type=" + type +
            ", partition=" + partition + "]");
    }
}

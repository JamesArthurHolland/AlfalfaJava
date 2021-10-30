package com.github.jamesarthurholland.alfalfa.typeSystem;

import java.util.ArrayList;
import java.util.HashMap;

public class TypeSystem
{
    public static enum Type {
        BOOL,
        DATE,
        INT,
        INT4,
        INT16,
        INT32,
        INT64,
        UINT,
        UINT4,
        UINT16,
        UINT32,
        UINT64,
        STRING,
    }

    public static HashMap<String, Type> types = new HashMap<>();

    static {
        types.put("bool", Type.BOOL);
        types.put("date", Type.DATE);
        types.put("int", Type.INT);
        types.put("int4", Type.INT4);
        types.put("int16", Type.INT16);
        types.put("int32", Type.INT32);
        types.put("int64", Type.INT64);
        types.put("uint", Type.UINT);
        types.put("uint4", Type.UINT4);
        types.put("uint16", Type.UINT16);
        types.put("uint32", Type.UINT32);
        types.put("uint64", Type.UINT64);

        types.put("string", Type.STRING);
    }
}

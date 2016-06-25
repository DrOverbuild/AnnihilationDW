package com.nekrosius.annihilationdw.database.mysql;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class QueryResult extends ArrayList<QueryResult.ResultData> {

    private static final long serialVersionUID = 7113173738229061819L;
    private Map<String, Integer> map = new HashMap<String, Integer>();

    public QueryResult(ResultSet set) throws SQLException {
        if (set != null) {
            for (int i = 1; i <= set.getMetaData().getColumnCount(); i++) {
                map.put(set.getMetaData().getColumnLabel(i).toLowerCase(), i - 1);
            }
            while (set.next()) {
                ArrayList<Object> objects = new ArrayList<Object>();
                for (int i = 1; i <= map.size(); i++) {
                    objects.add(set.getObject(i));
                }
                add(new ResultData(objects));
            }
        }
    }

    public int getColumnCount() {
        return map.size();
    }

    public int getColumnByName(String name) {
        if (map.containsKey(name.toLowerCase())) {
            return map.get(name.toLowerCase());
        } else {
            throw new IllegalArgumentException("No column by the name of " + name + " exists!");
        }
    }

    public void unload() {
        this.clear();
        map.clear();
        map = null;
    }

    public ResultData first() {
        return size() > 0 ? get(0) : null;
    }

    public class ResultData {

        private final ArrayList<Object> objects;

        public ResultData(ArrayList<Object> objects) {
            this.objects = objects;
        }

        public Object asObject(String name) {
            return asObject(getColumnByName(name));
        }

        public Object asObject(int column) {
            return objects.get(column);
        }

        public boolean asBoolean(String name) {
            return asBoolean(getColumnByName(name));
        }

        public boolean asBoolean(int column) {
            return Boolean.parseBoolean(asString(column));
        }

        public String asString(String name) {
            return asString(getColumnByName(name));
        }

        public String asString(int column) {
            Object obj = asObject(column);
            return obj != null ? obj.toString() : "";
        }

        public int asInt(String name) {
            return asInt(getColumnByName(name));
        }

        public int asInt(int column) {
            try {
                return Integer.parseInt(asString(column));
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            }
            return 0;
        }

        public BigDecimal asBigDecimal(String name) {
            return asBigDecimal(getColumnByName(name));
        }

        public BigDecimal asBigDecimal(int column) {
            try {
                return new BigDecimal(asString(column));
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            }
            return new BigDecimal(0d);
        }

        public byte asByte(String name) {
            return asByte(getColumnByName(name));
        }

        public byte asByte(int column) {
            try {
                return Byte.parseByte(asString(column));
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            }
            return 0;
        }

        public double asDouble(String name) {
            return asDouble(getColumnByName(name));
        }

        public double asDouble(int column) {
            try {
                return Double.parseDouble(asString(column));
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            }
            return 0d;
        }

        public short asShort(String name) {
            return asShort(getColumnByName(name));
        }

        public short asShort(int column) {
            try {
                return Short.parseShort(asString(column));
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            }
            return 0;
        }

        public long asLong(String name) {
            return asLong(getColumnByName(name));
        }

        public long asLong(int column) {
            try {
                return Long.parseLong(asString(column));
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            }
            return 0;
        }

        public Date asDate(String name) {
            return asDate(getColumnByName(name));
        }

        public Date asDate(int column) {
            try {
                return new Date(((java.sql.Date) asObject(column)).getTime());
            } catch (ClassCastException ex) {
                ex.printStackTrace();
            }
            return null;
        }

        public boolean hasColumn(String name) {
            try {
                getColumnByName(name);
            } catch (IllegalArgumentException ex) {
                return false;
            }
            return true;
        }

        public int getColumnCount() {
            return objects.size();
        }

    }

}

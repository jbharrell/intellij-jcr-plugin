package velir.intellij.cq5.jcr.model;

import com.day.cq.commons.date.DateUtil;
import com.day.cq.commons.date.InvalidDateException;
import com.intellij.openapi.diagnostic.Logger;
import org.apache.jackrabbit.value.*;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * @author krasmussen
 */
public class XMLProperty extends AbstractProperty {
	private static final Logger log = com.intellij.openapi.diagnostic.Logger.getInstance(XMLProperty.class);

    public XMLProperty(String name, Object value, String type){
        super(name, value, type);
    }

    private static String findType(String valueStr){
        String retType = STRING_PREFIX;
        if (valueStr.startsWith(BOOLEAN_PREFIX + "[")) {
            retType = BOOLEAN_ARRAY_PREFIX;
        } else if (valueStr.startsWith(BOOLEAN_PREFIX)) {
            retType = BOOLEAN_PREFIX;
        } else if (valueStr.startsWith(DOUBLE_PREFIX + "[")) {
            retType = DOUBLE_ARRAY_PREFIX;
        } else if (valueStr.startsWith(DOUBLE_PREFIX)) {
            retType = DOUBLE_PREFIX;
        } else if (valueStr.startsWith(LONG_PREFIX + "[")) {
            retType = LONG_ARRAY_PREFIX;
        } else if (valueStr.startsWith(LONG_PREFIX)) {
            retType = LONG_PREFIX;
        } else if(valueStr.startsWith(DATE_PREFIX)){
            retType = DATE_PREFIX;
        }
        else if (valueStr.startsWith("[")) {
            retType = STRING_ARRAY_PREFIX;
        }
        return retType;
    }

    private static Object findValue(String valueStr){
        Object retVal;
        // choose which type of object to insert
        if (valueStr.startsWith(BOOLEAN_PREFIX + "[")) {
            Boolean[] vals;
            String valuesString = valueStr.substring(0, valueStr.length() - 1).replaceFirst(Pattern.quote(BOOLEAN_PREFIX + "["), "");
            if ("".equals(valuesString)) vals = new Boolean[0];
            else {
                String[] valueBits = valuesString.split(",");
                vals = new Boolean[valueBits.length];
                for (int i = 0; i < valueBits.length; i++) {
                    vals[i] = Boolean.parseBoolean(valueBits[i]);
                }
            }
            retVal = vals;
        } else if (valueStr.startsWith(BOOLEAN_PREFIX)) {
            retVal = Boolean.parseBoolean(valueStr.replaceFirst(Pattern.quote(BOOLEAN_PREFIX), ""));
        } else if (valueStr.startsWith(DOUBLE_PREFIX + "[")) {
            Double[] vals;
            String valuesString = valueStr.substring(0, valueStr.length() - 1).replaceFirst(Pattern.quote(DOUBLE_PREFIX + "["), "");
            if ("".equals(valuesString)) vals = new Double[0];
            else {
                String[] valueBits = valuesString.split(",");
                vals = new Double[valueBits.length];
                for (int i = 0; i < valueBits.length; i++) {
                    vals[i] = Double.parseDouble(valueBits[i]);
                }
            }
            retVal = vals;
        } else if (valueStr.startsWith(DOUBLE_PREFIX)) {
            retVal = Double.parseDouble(valueStr.replaceFirst(Pattern.quote(DOUBLE_PREFIX), ""));
        } else if (valueStr.startsWith(LONG_PREFIX + "[")) {
            Long[] vals;
            String valuesString = valueStr.substring(0, valueStr.length() - 1).replaceFirst(Pattern.quote(LONG_PREFIX + "["), "");
            if ("".equals(valuesString)) vals = new Long[0];
            else {
                String[] valueBits = valuesString.split(",");
                vals = new Long[valueBits.length];
                for (int i = 0; i < valueBits.length; i++) {
                    vals[i] = Long.parseLong(valueBits[i]);
                }
            }
            retVal = vals;
        } else if (valueStr.startsWith(LONG_PREFIX)) {
            retVal = Long.parseLong(valueStr.replaceFirst(Pattern.quote(LONG_PREFIX), ""));
        } else if(valueStr.startsWith(DATE_PREFIX)){
            try{
                String dateStr = valueStr.replaceFirst(Pattern.quote(DATE_PREFIX), "");
                Calendar cal = DateUtil.parseISO8601(dateStr);
                retVal = cal.getTime();
            } catch (InvalidDateException e){
	            log.warn("Could not parse date", e);
                retVal = new Date();
            }
        }
        else if (valueStr.startsWith("[")) {
            String[] vals;
            String valuesString = valueStr.substring(1, valueStr.length() - 1);
            if ("".equals(valuesString)) vals = new String[0];
            else {
                vals = valuesString.split(",");
                for (int i = 0; i < vals.length; i++) {
                    vals[i] = unescapeSlashes(vals[i]);
                }
            }
            retVal = vals;
        } else {
            retVal = unescapeSlashes(valueStr);
        }
        return retVal;
    }

    // I'm not sure why escaping/unescaping is necessary
	private static String escapeSlashes (String s) {
		return s.replace("\\", "\\\\");
	}

    private static String unescapeSlashes (String s) {
		return s.replace("\\\\", "\\");
	}

    public String toString(){
        String retStr = "";
        Object value = this.getValue();
        if (value instanceof Long) {
			retStr = LONG_PREFIX + value.toString();
		} else if (value instanceof Boolean) {
			retStr = BOOLEAN_PREFIX + value.toString();
		} else if (value instanceof Double) {
			retStr = DOUBLE_PREFIX + value.toString();
		} else if (value instanceof Long[]) {
			String s = LONG_PREFIX + "[";
			Long[] ls = (Long[]) value;
			if (ls.length == 0) return s + "]";
			for (int i = 0; i < ls.length - 1; i++) {
				s += ls[i].toString() + ",";
			}
			retStr = s + ls[ls.length - 1] + "]";
		} else if (value instanceof Boolean[]) {
			String s = BOOLEAN_PREFIX + "[";
			Boolean[] ls = (Boolean[]) value;
			if (ls.length == 0) return s + "]";
			for (int i = 0; i < ls.length - 1; i++) {
				s += ls[i].toString() + ",";
			}
			retStr = s + ls[ls.length - 1] + "]";
		} else if (value instanceof Double[]) {
			String s = DOUBLE_PREFIX + "[";
			Double[] ls = (Double[]) value;
			if (ls.length == 0) return s + "]";
			for (int i = 0; i < ls.length - 1; i++) {
				s += ls[i].toString() + ",";
			}
			retStr = s + ls[ls.length - 1] + "]";
		} else if (value instanceof String[]) {
			String s = "[";
			String[] ss = (String[]) value;
			if (ss.length == 0) return s + "]";
			for (int i = 0; i < ss.length - 1; i++) {
				s += escapeSlashes(ss[i]) + ",";
			}
			retStr = s + escapeSlashes(ss[ss.length - 1]) + "]";
		} else if(value instanceof Date){
            //In format 2009-03-17T11:03:04.849+01:00
            retStr = DATE_PREFIX + DateUtil.getISO8601Date((Date) value);
        } else if(null != value){
			retStr = escapeSlashes(value.toString());
		}
        return retStr;
    }

    public static VProperty makeFromValueStr(String name, String valueStr){
        Object val = findValue(valueStr);
        String type = findType(valueStr);
        return new XMLProperty(name, val, type);
    }
}

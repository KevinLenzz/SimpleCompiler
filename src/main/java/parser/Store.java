package parser;
/**
 * 存放各种集合
 */

import java.util.*;

public class Store {
    public static HashMap<String,HashSet<String>> FIRST=new HashMap<String,HashSet<String>>();
    public static String S;
    public static HashMap<String,HashSet<String>> FOLLOW=new HashMap<String,HashSet<String>>();
    public static HashSet<String> VN=new HashSet<String>();
    public static HashSet<String> VT=new HashSet<String>();
    public static HashMap<String,ArrayList<String>> P=new HashMap<String,ArrayList<String>>();
    public static HashSet<String> NS=new HashSet<String>();
    public static HashMap<String,HashMap<String,ArrayList<String>>> M=new HashMap<String,HashMap<String,ArrayList<String>>>();
    public static HashMap<String,HashSet<String>> synchronizeSet=new HashMap<String,HashSet<String>>();
    public static HashMap<String,String> ErrorMap=new HashMap<String,String>();
}

package com.linking.global.util;

public class StringComparison {

    private static final String UPDATE = "UPDATE";
    private static final String INSERT = "INSERT";

    public static void compareString(String str1, String str2) {

        if (str1.length() < str2.length()) {
            str2isLonger(str1, str2);

        } else if (str1.length() > str2.length()) {
            str1isLonger(str1, str2);

        } else {
            equalLength(str1, str2);
        }
    }

    private static DiffStr str1isLonger(String str1, String str2) {

        // todo str2를 기준으로 돌려서 diffStartIndex 구한다.
        int diffStartIndex = -1;
        int diffEndIndex = -1;
        String modifiedSubStr = null;

        for (int i = 0; i < str2.length(); i++) {
            if (str1.charAt(i) != str2.charAt(i)) {
                diffStartIndex = i;
                break;
            }
        }
        // diffStartIndex == -1 이면 삭제된것
        // 이때 diffStartIndex = str2.length 이며, diffEndIndex = str1.length - 1
        if (diffStartIndex == -1) {
            diffStartIndex = str2.length();
            diffEndIndex = str1.length() - 1;
            modifiedSubStr = "";
        }
        // diffEndIndex = str1.length - 1 & modifiedSubStr = str2.subString(diffStartIndex)
        else {
            diffEndIndex = str1.length() - 1;
            modifiedSubStr = str2.substring(diffStartIndex);
        }
//        System.out.println("diffStartIndex = " + diffStartIndex);
//        System.out.println("diffEndIndex = " + diffEndIndex);
//        System.out.println("modifiedSubStr = " + modifiedSubStr);

        return new DiffStr(UPDATE, diffStartIndex, diffEndIndex, modifiedSubStr);

    }


    private static DiffStr str2isLonger(String str1, String str2) {

        int diffStartIndex = -1;
        int diffEndIndex = -1;
        String modifiedSubStr = null;
        boolean isInsert = false;

        // str1을 돌려 diffStartIndex 구한다.
        for (int i = 0; i < str1.length(); i++) {
            if (str1.charAt(i) != str2.charAt(i)) {
                diffStartIndex = i;
                break;
            }
        }

        // diffStartIndex == -1 이면 diffStartIndex = str1.length & isInsert = true 해준다.
        if (diffStartIndex == -1) {
            diffStartIndex = str1.length();
            isInsert = true;
        }

        modifiedSubStr = str2.substring(diffStartIndex);

//        System.out.println("diffStartIndex = " + diffStartIndex);
//        System.out.println("diffEndIndex = " + (str1.length() - 1));
//        System.out.println("modifiedSubStr = " + modifiedSubStr);
//        System.out.println("isInsert = " + isInsert);

        if (isInsert)
            return new DiffStr(INSERT, diffStartIndex, modifiedSubStr);
        else
            return new DiffStr(INSERT, diffStartIndex, diffEndIndex, modifiedSubStr);

    }

    private static DiffStr equalLength(String str1, String str2) {

        int diffStartIndex = -1;
        int diffEndIndex = -1;
        String modifiedSubStr = null;

        // str1 돌려서 diffStartIndex 찾기
        // diffStartIndex == -1 이면 변경없음
        // diffEndIndex 는 다른거 있을 때마다 갱신

        int i = 0;
        for (; i < str1.length(); i++) {
            if (str1.charAt(i) != str2.charAt(i)) {
                diffStartIndex = i;
                diffEndIndex = i;
                break;
            }
        }
        for (; i < str1.length(); i++) {
            if (str1.charAt(i) != str2.charAt(i)) {
                diffEndIndex = i;
            }
        }

        if (diffStartIndex == -1) {
//            System.out.println("Not Modified");
        } else {
            modifiedSubStr = str2.substring(diffStartIndex, diffEndIndex + 1);
//            System.out.println("diffStartIndex = " + diffStartIndex);
//            System.out.println("diffEndIndex = " + diffEndIndex);
//            System.out.println("modifiedSubStr = " + modifiedSubStr);
        }
        return new DiffStr(INSERT, diffStartIndex, diffEndIndex, modifiedSubStr);
    }
}

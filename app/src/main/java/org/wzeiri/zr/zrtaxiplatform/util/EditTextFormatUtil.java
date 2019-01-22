package org.wzeiri.zr.zrtaxiplatform.util;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author k-lm on 2017/11/24.
 */

public class EditTextFormatUtil {
    private EditTextFormatUtil() {

    }

    /**
     * 身份证格式
     */
    public static void formatIdCard(final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = s.toString();

                if (TextUtils.isEmpty(str)) {
                    return;
                }

                String reqStr = "[^0-9xX]";
                String content = str;

                if (str.length() < 18) {
                    reqStr = "[^0-9]";
                } else if (str.length() > 18) {
                    content = content.substring(0, 18);
                }

                content = replaceAll(content, reqStr);

                if (!TextUtils.equals(str, content)) {
                    editText.setText(content);
                    editText.setSelection(start);
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 手机号码格式
     */
    public static void formatPhone(final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = s.toString();

                if (TextUtils.isEmpty(str)) {
                    return;
                }

                String reqStr = "[^0-9]";
                String content = str;

                if (str.length() > 11) {
                    content = content.substring(0, 11);
                }

                content = replaceAll(content, reqStr);

                if (!TextUtils.equals(str, content)) {
                    editText.setText(content);
                    editText.setSelection(start);
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    /**
     * 格式为数字加字母
     */
    public static void formatNumberLetter(final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = s.toString();
                if (TextUtils.isEmpty(str)) {
                    return;
                }
                int strCount = str.length();

                StringBuilder contentBuilder = new StringBuilder();

                for (int i = 0; i < strCount; i++) {
                    char itemChar = str.charAt(i);
                    if ((itemChar >= 48 && itemChar <= 57) ||
                            (itemChar >= 65 && itemChar <= 90) ||
                            (itemChar >= 97 && itemChar <= 122)) {

                        contentBuilder.append(itemChar);

                    }

                }

                String content = contentBuilder.toString();

                if (!TextUtils.equals(content, str)) {
                    editText.setText(content);
                    editText.setSelection(start + 1);
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    /**
     * 格式为数字加大写字母
     */
    public static void formatNumberCapitalLetter(final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = s.toString();
                if (TextUtils.isEmpty(str)) {
                    return;
                }
                int strCount = str.length();

                StringBuilder contentBuilder = new StringBuilder();

                for (int i = 0; i < strCount; i++) {
                    char itemChar = str.charAt(i);
                    if ((itemChar >= 48 && itemChar <= 57) ||
                            (itemChar >= 65 && itemChar <= 90) ||
                            (itemChar >= 97 && itemChar <= 122)) {
                        if (itemChar >= 97) {
                            itemChar -= 32;
                        }
                        contentBuilder.append(itemChar);

                    }

                }

                String content = contentBuilder.toString();

                if (!TextUtils.equals(content, str)) {
                    editText.setText(content);
                    if (start > 0 ||
                            (start == 0 && content.length() > 0)) {
                        start = Math.min(start + 1, content.length());
                        editText.setSelection(start);
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    /**
     * 格式为数字加小写字母
     */
    public static void formatNumberLowercaseLetter(final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = s.toString();
                if (TextUtils.isEmpty(str)) {
                    return;
                }
                int strCount = str.length();

                StringBuilder contentBuilder = new StringBuilder();

                for (int i = 0; i < strCount; i++) {
                    char itemChar = str.charAt(i);
                    if ((itemChar >= 48 && itemChar <= 57) ||
                            (itemChar >= 65 && itemChar <= 90) ||
                            (itemChar >= 97 && itemChar <= 122)) {
                        if (itemChar >= 65 && itemChar <= 97) {
                            itemChar += 32;
                        }
                        contentBuilder.append(itemChar);

                    }

                }

                String content = contentBuilder.toString();

                if (!TextUtils.equals(content, str)) {
                    editText.setText(content);
                    editText.setSelection(start + 1);
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    /**
     * 去除 emoji 表情
     *
     * @param editText
     */
    public static void formatRemoveEmoji(final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = s.toString();

                if (TextUtils.isEmpty(str)) {
                    return;
                }

                String reqStr = "[^\\u0000-\\uFFFF]";
                String content = replaceAll(str, reqStr);

                if (TextUtils.equals(content, str)) {
                    return;
                }
                editText.setText(content);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    /**
     * 返回正则过滤后的结果
     *
     * @param content 要过滤的内容
     * @param regex   正则表达式
     * @return 返回过滤后的内容
     */
    public static String replaceAll(String content, String regex) {
        return replaceAll(content, regex, "");
    }


    /**
     * 返回正则过滤后的结果
     *
     * @param content     要过滤的内容
     * @param regex       正则表达式
     * @param replacement 替换内容
     * @return 返回过滤后的内容
     */
    public static String replaceAll(String content, String regex, String replacement) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(content);
        return m.replaceAll(replacement);
    }


}

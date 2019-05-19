
package com.joomag.test.model.remote;

import androidx.annotation.Nullable;

public class Condition {


    private Long code;

    private String icon;

    private String text;

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == this) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (obj instanceof Condition) {
            Condition condition = (Condition) obj;
            return condition.getCode().equals(code) && condition.getIcon().equals(icon) &&
                    condition.getText().equals(text);
        }
        return super.equals(obj);
    }
}

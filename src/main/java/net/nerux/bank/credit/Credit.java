package net.nerux.bank.credit;

import java.sql.Date;

import net.nerux.Serializable;
import org.json.simple.JSONObject;

public class Credit implements Serializable {

    private Date start;
    private Date back;
    private CreditType type;
    private int strafe;

    public Credit(final CreditType type, final Date start, final int strafe) {
        this.type = type;
        this.start = start;
        this.strafe = strafe;
    }

    public Credit(final JSONObject ob) {
        this.type = CreditType.getCreditTypeByName((String) ob.get("type"));
        this.start = new Date(((Long) ob.get("start")));
        this.strafe = (int) ((long) ob.get("strafe"));
    }

    public CreditType getType() {
        return type;
    }

    public void setType(final CreditType type) {
        this.type = type;
    }

    @SuppressWarnings("deprecation")
    public Date getBack() {
        if (back == null) {
            back = new Date(start.getTime());
            back.setDate(back.getDate() + type.getDays());
        }
        return back;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public int getStrafe() {
        return strafe;
    }

    public void setStrafe(final int strafe) {
        this.strafe = strafe;
    }

    @SuppressWarnings("unchecked")
    public JSONObject serialize() {
        JSONObject ob = Serializable.serialize();

        ob.put("type", type.getName());
        ob.put("start", start.getTime());
        ob.put("isinjail", false);
        ob.put("zeitinjail", 0);

        return ob;
    }

}
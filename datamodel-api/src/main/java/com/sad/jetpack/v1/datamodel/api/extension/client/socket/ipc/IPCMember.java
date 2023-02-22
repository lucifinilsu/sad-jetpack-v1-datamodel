package com.sad.jetpack.v1.datamodel.api.extension.client.socket.ipc;

import org.json.JSONObject;

public class IPCMember {
    private String id="";//端id
    private String description="";//端描述
    private String Alias ="";//端代号
    private JSONObject extra=new JSONObject();//附加信息

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAlias() {
        return Alias;
    }

    public void setAlias(String alias) {
        Alias = alias;
    }

    public JSONObject getExtra() {
        return extra;
    }

    public void setExtra(JSONObject extra) {
        this.extra = extra;
    }

    public static IPCMember parse(JSONObject jsonObject){
        IPCMember member=new IPCMember();
        member.setId(jsonObject.optString("id"));
        member.setAlias(jsonObject.optString("alias"));
        member.setDescription(jsonObject.optString("description"));
        member.setExtra(jsonObject.optJSONObject("extra"));
        return member;
    }

    public JSONObject toJson(){
       try {
           JSONObject json=new JSONObject();
           json.put("id",getId());
           json.put("alias",getAlias());
           json.put("description",getDescription());
           json.put("extra",getExtra());
           return json;
       }catch (Exception e){
           e.printStackTrace();
       }
       return null;
    }
}

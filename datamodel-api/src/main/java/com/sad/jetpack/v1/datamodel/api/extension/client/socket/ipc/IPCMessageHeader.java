package com.sad.jetpack.v1.datamodel.api.extension.client.socket.ipc;

import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class IPCMessageHeader {
    public final static int TYPE_TEXT=0;//文本消息
    public final static int TYPE_IMAGE=1;//图片消息
    public final static int TYPE_VIDEO=2;//视频消息
    public final static int TYPE_AUDIO=3;//音频消息
    public final static int TYPE_LINK=4;//链接消息
    public final static int TYPE_HTML=5;//HTML消息
    public final static int TYPE_PROGRAM=6;//程序消息

    private String id="";//消息id
    private int type=TYPE_TEXT;//消息类型
    private int version=1;//消息版本
    private IPCMember from=new IPCMember();//消息来源
    private List<IPCMember> to=new ArrayList<>();//消息发送目标
    private String time="";//消息发送时间
    private String description="";//消息描述
    private long bodySize =-1;//消息体长度
    private JSONObject extra=new JSONObject();//附加信息

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public IPCMember getFrom() {
        return from;
    }

    public void setFrom(IPCMember from) {
        this.from = from;
    }

    public List<IPCMember> getTo() {
        return to;
    }

    public void setTo(List<IPCMember> to) {
        this.to = to;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getBodySize() {
        return bodySize;
    }

    public void setBodySize(long bodySize) {
        this.bodySize = bodySize;
    }

    public JSONObject getExtra() {
        return extra;
    }

    public void setExtra(JSONObject extra) {
        this.extra = extra;
    }
    public static IPCMessageHeader parse(String s){
        return parse(s.getBytes(StandardCharsets.UTF_8));
    }
    public static IPCMessageHeader parse(byte[] bytes){
        String s=new String(bytes);
        try {
            IPCMessageHeader header=new IPCMessageHeader();
            JSONObject json=new JSONObject(s);
            header.setId(json.optString("id"));
            header.setType(json.optInt("type",TYPE_TEXT));
            header.setVersion(json.optInt("version",1));
            header.setFrom(IPCMember.parse(json.optJSONObject("from")));
            header.setDescription(json.optString("description"));
            header.setTime(json.optString("time"));
            header.setBodySize(json.optLong("bodySize"));
            header.setExtra(json.optJSONObject("extra"));
            JSONArray ja=json.optJSONArray("to");
            List<IPCMember> toMembers=new ArrayList<>();
            for (int i = 0; i < ja.length(); i++) {
                JSONObject j=ja.optJSONObject(i);
                IPCMember toMember=IPCMember.parse(j);
                toMembers.add(toMember);
            }
            header.setTo(toMembers);

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject toJson(){
        try {
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("id",getId());
            jsonObject.put("type",getType());
            jsonObject.put("version",getVersion());
            jsonObject.put("from",getFrom().toJson());
            JSONArray ja_to=new JSONArray();
            for (IPCMember toMember:getTo()
                 ) {
                ja_to.put(toMember.toJson());
            }
            jsonObject.put("to",ja_to);
            jsonObject.put("description",getDescription());
            jsonObject.put("time",getTime());
            jsonObject.put("bodySize",getBodySize());
            jsonObject.put("extra",getExtra());
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return null;
    }

}

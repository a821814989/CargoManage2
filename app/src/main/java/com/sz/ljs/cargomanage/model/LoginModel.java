package com.sz.ljs.cargomanage.model;

import java.util.List;

/**
 * Created by Administrator on 2018/8/10.
 */

public class LoginModel {

    /**
     * Code : 1
     * Msg : OK
     * Data : {"UserModel":{"authentication_code":null,"tms_id":33,"st_id_ctreate":1,"sp_id":2,"ur_loginpassword":"e10adc3949ba59abbe56e057f20f883e","st_ename":"管理员","st_name":"管理员","competence_og_id":1,"og_id":12,"vs_code":"R","st_code":"ADMIN","st_id":69,"ding_user_id":"102524202232465626","og_shortcode":"101SZX"},"tokenModel":{"id":null,"st_id":69,"token":"ef9d9139-7e7c-4f03-acfb-bc0ed35aaa70","failure_time":"1533970548","create_date":"2018-08-10T14:55:49.8141469+08:00"},"permission":[{"mi_name":"入库","mi_ename":"Chenck In"}]}
     */

    private int Code;
    private String Msg;
    private DataEntity Data;

    public int getCode() {
        return Code;
    }

    public void setCode(int Code) {
        this.Code = Code;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String Msg) {
        this.Msg = Msg;
    }

    public DataEntity getData() {
        return Data;
    }

    public void setData(DataEntity Data) {
        this.Data = Data;
    }

    public static class DataEntity {
        /**
         * UserModel : {"authentication_code":null,"tms_id":33,"st_id_ctreate":1,"sp_id":2,"ur_loginpassword":"e10adc3949ba59abbe56e057f20f883e","st_ename":"管理员","st_name":"管理员","competence_og_id":1,"og_id":12,"vs_code":"R","st_code":"ADMIN","st_id":69,"ding_user_id":"102524202232465626","og_shortcode":"101SZX"}
         * tokenModel : {"id":null,"st_id":69,"token":"ef9d9139-7e7c-4f03-acfb-bc0ed35aaa70","failure_time":"1533970548","create_date":"2018-08-10T14:55:49.8141469+08:00"}
         * permission : [{"mi_name":"入库","mi_ename":"Chenck In"}]
         */

        private UserModelEntity UserModel;
        private TokenModelEntity tokenModel;
        private List<PermissionEntity> permission;

        public UserModelEntity getUserModel() {
            return UserModel;
        }

        public void setUserModel(UserModelEntity UserModel) {
            this.UserModel = UserModel;
        }

        public TokenModelEntity getTokenModel() {
            return tokenModel;
        }

        public void setTokenModel(TokenModelEntity tokenModel) {
            this.tokenModel = tokenModel;
        }

        public List<PermissionEntity> getPermission() {
            return permission;
        }

        public void setPermission(List<PermissionEntity> permission) {
            this.permission = permission;
        }

        public static class UserModelEntity {
            /**
             * authentication_code : null
             * tms_id : 33
             * st_id_ctreate : 1
             * sp_id : 2
             * ur_loginpassword : e10adc3949ba59abbe56e057f20f883e
             * st_ename : 管理员
             * st_name : 管理员
             * competence_og_id : 1
             * og_id : 12
             * vs_code : R
             * st_code : ADMIN
             * st_id : 69
             * ding_user_id : 102524202232465626
             * og_shortcode : 101SZX
             */

            private Object authentication_code;
            private int tms_id;
            private int st_id_ctreate;
            private int sp_id;
            private String ur_loginpassword;
            private String st_ename;
            private String st_name;
            private int competence_og_id;
            private int og_id;
            private String vs_code;
            private String st_code;
            private int st_id;
            private String ding_user_id;
            private String og_shortcode;
            private String og_cityenname;

            public Object getAuthentication_code() {
                return authentication_code;
            }

            public void setAuthentication_code(Object authentication_code) {
                this.authentication_code = authentication_code;
            }

            public int getTms_id() {
                return tms_id;
            }

            public void setTms_id(int tms_id) {
                this.tms_id = tms_id;
            }

            public int getSt_id_ctreate() {
                return st_id_ctreate;
            }

            public void setSt_id_ctreate(int st_id_ctreate) {
                this.st_id_ctreate = st_id_ctreate;
            }

            public int getSp_id() {
                return sp_id;
            }

            public void setSp_id(int sp_id) {
                this.sp_id = sp_id;
            }

            public String getUr_loginpassword() {
                return ur_loginpassword;
            }

            public void setUr_loginpassword(String ur_loginpassword) {
                this.ur_loginpassword = ur_loginpassword;
            }

            public String getSt_ename() {
                return st_ename;
            }

            public void setSt_ename(String st_ename) {
                this.st_ename = st_ename;
            }

            public String getSt_name() {
                return st_name;
            }

            public void setSt_name(String st_name) {
                this.st_name = st_name;
            }

            public int getCompetence_og_id() {
                return competence_og_id;
            }

            public void setCompetence_og_id(int competence_og_id) {
                this.competence_og_id = competence_og_id;
            }

            public int getOg_id() {
                return og_id;
            }

            public void setOg_id(int og_id) {
                this.og_id = og_id;
            }

            public String getVs_code() {
                return vs_code;
            }

            public void setVs_code(String vs_code) {
                this.vs_code = vs_code;
            }

            public String getSt_code() {
                return st_code;
            }

            public void setSt_code(String st_code) {
                this.st_code = st_code;
            }

            public int getSt_id() {
                return st_id;
            }

            public void setSt_id(int st_id) {
                this.st_id = st_id;
            }

            public String getDing_user_id() {
                return ding_user_id;
            }

            public void setDing_user_id(String ding_user_id) {
                this.ding_user_id = ding_user_id;
            }

            public String getOg_shortcode() {
                return og_shortcode;
            }

            public void setOg_shortcode(String og_shortcode) {
                this.og_shortcode = og_shortcode;
            }

            public String getOg_cityenname() {
                return og_cityenname;
            }

            public void setOg_cityenname(String og_cityenname) {
                this.og_cityenname = og_cityenname;
            }
        }

        public static class TokenModelEntity {
            /**
             * id : null
             * st_id : 69
             * token : ef9d9139-7e7c-4f03-acfb-bc0ed35aaa70
             * failure_time : 1533970548
             * create_date : 2018-08-10T14:55:49.8141469+08:00
             */

            private Object id;
            private int st_id;
            private String token;
            private String failure_time;
            private String create_date;

            public Object getId() {
                return id;
            }

            public void setId(Object id) {
                this.id = id;
            }

            public int getSt_id() {
                return st_id;
            }

            public void setSt_id(int st_id) {
                this.st_id = st_id;
            }

            public String getToken() {
                return token;
            }

            public void setToken(String token) {
                this.token = token;
            }

            public String getFailure_time() {
                return failure_time;
            }

            public void setFailure_time(String failure_time) {
                this.failure_time = failure_time;
            }

            public String getCreate_date() {
                return create_date;
            }

            public void setCreate_date(String create_date) {
                this.create_date = create_date;
            }
        }

        public static class PermissionEntity {
            /**
             * mi_name : 入库
             * mi_ename : Chenck In
             */

            private String mi_name;
            private String mi_ename;

            public String getMi_name() {
                return mi_name;
            }

            public void setMi_name(String mi_name) {
                this.mi_name = mi_name;
            }

            public String getMi_ename() {
                return mi_ename;
            }

            public void setMi_ename(String mi_ename) {
                this.mi_ename = mi_ename;
            }
        }
    }
}

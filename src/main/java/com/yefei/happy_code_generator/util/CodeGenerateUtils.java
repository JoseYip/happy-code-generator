package com.yefei.happy_code_generator.util;

import com.sun.xml.internal.ws.util.StringUtils;
import com.yefei.happy_code_generator.model.DictionaryGroupDTO;
import com.yefei.happy_code_generator.model.DictionaryInfoDTO;
import freemarker.template.Template;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CodeGenerateUtils {

    private final String AUTHOR = "yefei";
    private final String CURRENT_DATE = LocalDate.now().toString();
    private final String tableName = "comm_dictionary_info";
    private final String packageName = "com.yefei.happy_code_generator.model.enums";
    private final String tableAnnotation = "捡垃圾吃";
    private final String URL = "jdbc:mysql://";
    private final String USER = "";
    private final String PASSWORD = "";
    private final String DRIVER = "com.mysql.jdbc.Driver";
    private final String diskPath = "/Users/feiye/Work/iyin/svn/happy-code-generator/src/main/java/com/yefei/happy_code_generator/model/enums";
    private final String changeTableName = replaceUnderLineAndUpperCase(tableName);

    public Connection getConnection() throws Exception {
        Class.forName(DRIVER);
        Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
        return connection;
    }

    public void generate() {
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from comm_dictionary_group");

            generateEnum(resultSet);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {

        }
    }

    private void generateEnum(ResultSet resultSet) throws Exception {
        final String suffix = ".java";
        final String templateName = "ENUM.ftl";

        while (resultSet.next()) {
            String groupName = resultSet.getString("group_name");
            final String path = diskPath + capitalFirstLetter(groupName) + "Enum" + suffix;
            File mapperFile = new File(path);

            DictionaryGroupDTO group = new DictionaryGroupDTO();
            List<DictionaryInfoDTO> infoList = new ArrayList<>();
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            String sql = String.format("select * from %s where dic_group_id = '%s'", "comm_dictionary_info", resultSet.getString("agency_pk"));
            ResultSet resultSet1 = statement.executeQuery(sql);
            while (resultSet1.next()) {
                DictionaryInfoDTO info = new DictionaryInfoDTO();
                info.setAgencyPk(resultSet1.getString("agency_pk"));
                info.setDicKey(resultSet1.getString("dic_key"));
                info.setDicValue(resultSet1.getString("dic_value"));
                info.setDicDescribe(resultSet1.getString("dic_describe"));
                info.setDicIndex(resultSet1.getInt("dic_index"));
                info.setDicName(capitalFirstLetter(resultSet1.getString("dic_describe")));
                infoList.add(info);
            }
            group.setAgencyPk(resultSet.getString("agency_pk"));
            group.setDescribe(resultSet.getString("group_describe"));
            group.setName(groupName);
            group.setChangeName(capitalFirstLetter(groupName) + "Enum");
            group.setDictionaryInfoDtoList(infoList);
//            columnClassList.add(group);
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("model_column", group);
            if (!infoList.isEmpty()) {
                generateFileByTemplate(templateName, mapperFile, dataMap);
            } else {
            }
        }

    }

    public String capitalFirstLetter(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public String replaceUnderLineAndUpperCase(String str) {
        StringBuffer sb = new StringBuffer();
        sb.append(str);
        int count = sb.indexOf("_");
        while (count != 0) {
            int num = sb.indexOf("_", count);
            count = num + 1;
            if (num != -1) {
                char ss = sb.charAt(count);
                char ia = (char) (ss - 32);
                sb.replace(count, count + 1, ia + "");
            }
        }
        String result = sb.toString().replaceAll("_", "");
        return StringUtils.capitalize(result);
    }

    private void generateFileByTemplate(final String templateName, File file, Map<String, Object> dataMap) throws Exception {
        Template template = FreeMarkerTemplateUtils.getTemplate(templateName);
        FileOutputStream fos = new FileOutputStream(file);
        dataMap.put("table_name_small", tableName);
        dataMap.put("table_name", changeTableName);
        dataMap.put("author", AUTHOR);
        dataMap.put("date", CURRENT_DATE);
        dataMap.put("package_name", packageName);
        dataMap.put("table_annotation", tableAnnotation);
        Writer out = new BufferedWriter(new OutputStreamWriter(fos, "utf-8"), 10240);
        template.process(dataMap, out);
    }
}

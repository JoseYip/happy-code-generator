package ${package_name};

/**
* 描述：${model_column.changeName}模型
* @author ${author}
* @date ${date}
*/

<#if model_column?exists>
public enum ${model_column.changeName} {


    <#list model_column.dictionaryInfoDtoList as model>
    /**
    *${model.dicDescribe!}
    */

    ${model.dicName}("${model.dicKey}","${model.dicValue}")<#if model_has_next>,</#if>

    </#list>;

    private String code;
    private String name;

    ${model_column.changeName}(String code , String name){
        this.code = code;
        this.name = name;
    }

    /**
     * 根据代码获取枚举名称
     * */
    public static String getNameByCode(String code){
        for (${model_column.changeName} thisEnum : ${model_column.changeName}.values()){
            if (thisEnum.getCode().equals(code)){
                return thisEnum.getName();
            }
        }
        return null;
    }

    /**
     * 根据名称获取枚举代码
     * */
    public static String getCodeByName(String name){
        for (${model_column.changeName} thisEnum : ${model_column.changeName}.values()){
            if (thisEnum.getName().equals(name)){
                return thisEnum.getCode();
            }
        }
        return null;
    }

    /**
     * 根据代码获取枚举对象
     * */
    public static ${model_column.changeName} get${model_column.changeName}ByCode(Integer code){
        for (${model_column.changeName} thisEnum : ${model_column.changeName}.values()){
            if (thisEnum.getCode().equals(code)){
                return thisEnum;
            }
        }
        return null;
    }

    /**
     * 根据名称获取枚举对象
     * */
    public static ${model_column.changeName} get${model_column.changeName}ByName(String name){
        for (${model_column.changeName} thisEnum : ${model_column.changeName}.values()){
            if (thisEnum.getName().equals(name)){
                return thisEnum;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}


</#if>


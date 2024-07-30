public class MethodParser {
    public static Method parse(String methodStr){
        if (methodStr.equals("GET")) return Method.GET;
        else if(methodStr.equals("POST")) return Method.POST;
        return Method.GET;
    }
}

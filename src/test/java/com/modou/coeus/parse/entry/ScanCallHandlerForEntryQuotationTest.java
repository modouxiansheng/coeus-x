package com.modou.coeus.parse.entry;

import com.modou.coeus.ability.scan.ScanCallChainAbility;
import com.modou.coeus.ability.scan.ScanCallHandlerForEntryQuotation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @program: coeus-x
 * @description:
 * @author: hu_pf
 * @create: 2023-08-03 10:45
 **/
public class ScanCallHandlerForEntryQuotationTest {


    public static void main(String[] args) {
        String projectRoot = "/Users/admin/mygit/coeus-x/target/test-classes/com/modou/coeus/parse/entry";
        ScanCallHandlerForEntryQuotation scanCallHandlerForValueAnnotation = new ScanCallHandlerForEntryQuotation();
        ScanCallChainAbility scanCallChainAbility = new ScanCallChainAbility(projectRoot,scanCallHandlerForValueAnnotation);
        scanCallChainAbility.invoke("com.modou.coeus.parse.entry.ScanCallHandlerForEntryQuotationTest","invoke");
        Map<String, Set<String>> result = scanCallHandlerForValueAnnotation.getResult();
        for (String key: result.keySet()){
            Set<String> params = result.get(key);
            for (String param :params){
                System.out.println(key+":"+param);
            }
        }
    }

    public void invoke(){
        Student student = new Student();
        List<Student.Order> orderList = new ArrayList<>();
        student.setName("xxx");
        student.setAge(123);
        student.setOrderList(orderList);

        if (Boolean.TRUE.equals(student.getFlag())){
            System.out.println("xxx");
        }
        student.getOrderList().forEach(e->{
            System.out.println(e.getNo());
        });
    }
}

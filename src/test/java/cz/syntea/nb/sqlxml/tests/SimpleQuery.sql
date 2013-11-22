 SELECT XMLELEMENT(NAME "Subjects",
                  XMLAGG(	
                  XMLELEMENT(NAME "Contact",
                    XMLATTRIBUTES(cust.EMPJMENO AS "Name",
                                cust.EMPPRIJMENI)
                              )
                        )
                  )
FROM DN.OR_SUBJEKTZAZNAM AS cust 
WHERE cust.EMPPRIJMENI LIKE 'Kec'

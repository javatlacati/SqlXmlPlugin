 SELECT XMLELEMENT(NAME "Subjects", -- root element name
                  XMLAGG(-- aggregation over the rows	
                  XMLELEMENT(NAME "Contact",
                    XMLATTRIBUTES(cust.EMPJMENO AS "Name",
                                cust.EMPPRIJMENI)
                              )
                        )
                  )
FROM DN.OR_SUBJEKTZAZNAM AS cust 
WHERE cust.EMPPRIJMENI LIKE 'Kec'

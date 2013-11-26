SELECT XMLELEMENT(NAME "Subjects",
                  XMLAGG( 
                  XMLELEMENT(NAME "Contact",
                    XMLATTRIBUTES(cust.TABNAME    AS "FirstName",
                                  cust.TABSCHEMA  AS "LastName")
                              ) 
                        )
                  )  
FROM  SYSCAT.TABLES AS cust
WHERE cust.TABSCHEMA LIKE 'DAN%'

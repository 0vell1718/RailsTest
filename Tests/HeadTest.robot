*** Settings ***
Variables    ../Other/Global.py
Library  ../Libraries/RestLibrary.py


*** Test Cases ***
Test Header
    [Template]  Send Header And assert Response Code and header
    # key       # value         #expected_value     # expected_response_code
    Somekey     Somevalue       Somevalue           200


*** Keywords ***
Send Header And assert Response Code and header
    [Arguments]  ${key}  ${value}  ${expected_value}  ${expected_response_code}
    ${value}  ${response}   Header  ${HOST}  ${key}  ${value}
    Should Be Equal  ${value}  ${expected_value}
    Should Be Equal  ${response}  ${expected_response_code}

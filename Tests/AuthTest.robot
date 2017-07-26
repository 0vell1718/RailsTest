*** Settings ***
Variables    ../Other/Global.py
Library  ../Libraries/RestLibrary.py


*** Test Cases ***
Test Auth
    [Template]  Authenticate And assert Response Code
    # user      # password      # expected_response_code
    user        passwd          200
    users       passwd          401
    user        123qsd          401
    users       asddda          401
    ${EMPTY}    passwd          401
    user        ${EMPTY}        401
    ${EMPTY}    ${EMPTY}        401


*** Keywords ***
Authenticate And assert Response Code
    [Arguments]  ${user}  ${password}  ${expected_response_code}
    ${status} =  Authenticate  ${HOST}  ${user}  ${password}
    Should Be Equal  ${status}  ${expected_response_code}



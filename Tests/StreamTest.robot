*** Settings ***
Variables    ../Other/Global.py
Library  ../Libraries/RestLibrary.py


*** Test Cases ***
Test Stream
    [Template]  Stream And Assert Response Code And Lines Count
    # send_count        # expected_count        # expected_response_code
    10                  10                      200
    ten                 0                       404
    ${empty}            0                       404

*** Keywords ***
Stream And Assert Response Code And Lines Count
    [Arguments]  ${send_count}  ${expected_count}  ${expected_response_code}
    ${count}  ${response}   Stream  ${HOST}  ${send_count}
    Should Be Equal  ${count}  ${expected_count}
    Should Be Equal  ${response}  ${expected_response_code}

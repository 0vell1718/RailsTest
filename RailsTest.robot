*** Settings ***
Variables  Other/Global.py
Library  Libriary/BaseElement.py    suite       ${HOST}     ${USER}    ${PASSWORD}     WITH NAME       suite
Library  Libriary/BaseElement.py    run         ${HOST}     ${USER}    ${PASSWORD}     WITH NAME       run
Library  Libriary/BaseElement.py    section     ${HOST}     ${USER}    ${PASSWORD}     WITH NAME       section
Library  Libriary/BaseElement.py    case        ${HOST}     ${USER}    ${PASSWORD}     WITH NAME       case
Library  Other/Random.py            ${Random_name_size}                                WITH NAME       random

*** Variables ***
@{suite_fields} =       name        description
@{section_fields} =     name        description
@{case_fields} =        title       template_id     type_id     priority_id     refs
@{run_fields} =         name        description
@{result_fields} =      status_id   comment         version     defects

*** Test Cases ***
My Test
    Create Suite With Random Fields Value  @{suite_fields}
    ${SUITE_ID} =  suite.GetId
    Assert Suite Is Present

    Create Section With SuiteID And Random Fields Value  ${SUITE_ID}  @{section_fields}
    ${SECTION_ID} =  section.GetId
    Assert Section Is Present

    ${random_fields} =  Create Case With SectionID And Random Fields Value  ${SECTION_ID}  @{case_fields}
    ${CASE_ID} =  case.GetId
    Assert Case Fields  ${random_fields}  @{case_fields}
    Assert Case Is Present

    Create Run With SuiteId and Random Fields Value  ${SUITE_ID}  @{run_fields}
    ${RUN_ID} =  run.GetId
    Assert Run Is Present

    Add Result For Case With RunID, CaseID And Random Fields Value  ${RUN_ID}  ${CASE_ID}  @{result_fields}

    Delete Elements

*** Keywords ***
Create Suite With Random Fields Value
    [Arguments]  @{fields}
    ${random_fields} =  random.Set   @{fields}
    suite.Add  ${None}  ${random_fields}

Assert Suite Is Present
    ${response} =  suite.GetStatusCode
    Should Be Equal  ${200}  ${response}

Create Section With SuiteID And Random Fields Value
    [Arguments]   ${SUITE_ID}  @{fields}
    @{field} =  Create List    name   description
    ${fields} =  random.Set   @{field}
    section.Add  ${SUITE_ID}  ${fields}

Assert Section Is Present
    ${response} =  section.GetStatusCode
    Should Be Equal  ${200}  ${response}

Create Run With SuiteId and Random Fields Value
    [Arguments]  ${SUITE_ID}  @{fields}
    ${random_fields} =  random.Set   @{fields}
    run.Add  ${SUITE_ID}  ${random_fields}

Assert Run Is Present
    ${response} =  run.GetStatusCode
    Should Be Equal  ${200}  ${response}

Create Case With SectionID And Random Fields Value
    [Arguments]   ${SECTION_ID}  @{fields}
    ${random_fields} =  random.Set   @{fields}
    case.Add  ${SECTION_ID}  ${random_fields}
    return from keyword  ${random_fields}

Assert Case Is Present
    ${response} =  case.GetStatusCode
    Should Be Equal  ${200}  ${response}

Assert Case Fields
    [Arguments]  ${random_fields}  @{fields}
    ${case_fields} =  case.GetFields  @{fields}
    Should Be Equal  ${random_fields}  ${case_fields}

Add Result For Case With RunID, CaseID And Random Fields Value
    [Arguments]   ${RUN_ID}  ${CASE_ID}  @{fields}
    ${random_fileds} =  random.Set   @{fields}
    case.AddResult   ${RUN_ID}  ${CASE_ID}  ${random_fileds}

Delete Elements
    run.Delete
    case.Delete
    section.Delete
    suite.Delete
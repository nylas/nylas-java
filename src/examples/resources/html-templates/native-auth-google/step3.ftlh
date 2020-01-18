<#include "base.ftlh">

<@display_page>
  <h2>Done!</h2>
  <p>You've successfully connected to Nylas via native authentication!
    Here's some account information that I got from the Nylas API, to prove it:</p>

  <table class="table table-bordered">
    <tr><th>id</th><td>${accountDetail.id}</td></tr>
    <tr><th>name</th><td>${accountDetail.name}</td></tr>
    <tr><th>emailAddress</th><td>${accountDetail.emailAddress}</td></tr>
    <tr><th>provider</th><td>${accountDetail.provider}</td></tr>
    <tr><th>organizationUnit</th><td>${accountDetail.organizationUnit}</td></tr>
    <tr><th>syncState</th><td>${accountDetail.syncState}</td></tr>
    <tr><th>linkedAt</th><td>${accountDetail.linkedAt}</td></tr>
  </table>

</@display_page>
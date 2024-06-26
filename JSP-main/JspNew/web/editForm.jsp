<jsp:useBean class="model.Student"  id="s" />
<%@page  import="dao.StudentDao" %>
<jsp:setProperty  name="s" property="*" />


<%@include file="header.jsp" %>



<% 
    String id=request.getParameter("id");
    s=StudentDao.getStudentbyId(Integer.parseInt(id));
%>

<div class="container my-3">
    <div class="bg-warning text-center">
        <h1 class="jumborton">Edit Student</h1>
    </div>
    <!--    start form-->
    <form action="edit.jsp"  method="post">
        <input type="hidden" class="form-control" id="id" name="id" value="<%=s.getId()%>" >


        <div class="row">
            <div class="col-md-6">
                <label for="exampleInputEmail1" class="form-label">First Name</label>
                <input type="text" class="form-control" id="givenName" name="givenName" value="<%=s.getGivenName()%>" placeholder="First Name">
            </div>

            <div class="col-md-6">
                <label for="exampleInputEmail1" class="form-label">Last Name</label>
                <input type="text" class="form-control" id="lastName" name="lastName" value="<%=s.getLastName()%>" placeholder="Last Name">
            </div>
        </div>
        <!-- start 2nd row-->
        <div class="row mt-3">
            <div class="col-md-6">
                <label  class="form-label">Subject</label>
                <select class="form-select" name="subject" aria-label="Default select example" value="<%=s.getSubject()%>">
                    <option selected>Select Anyone</option>
                    <option value="JEE" <%= s.getSubject().equals("JEE") ? "selected" : "" %>>JEE</option>
                    <option value="NT" <%= s.getSubject().equals("NT") ? "selected" : "" %>>NT</option>
                    <option value="DDD" <%= s.getSubject().equals("DDD") ? "selected" : "" %>>DDD</option>
                    <option value="GAVE" <%= s.getSubject().equals("GAVE") ? "selected" : "" %>>GAVE</option>
                </select>
            </div>

            <div class="col-md-6">
                <label for="exampleInputEmail1" class="form-label">Gender</label>
                <div class="form-check">
                    <input class="form-check-input" type="radio" value="Male" name="gender" id="flexRadioDefault1" <%= s.getGender().equals("Male") ? "checked" : "" %>>
                    <label class="form-check-label" for="flexRadioDefault1">Male</label>
                </div>
                <div class="form-check">
                    <input class="form-check-input" type="radio" value="Female" name="gender" id="flexRadioDefault2" <%= s.getGender().equals("Female") ? "checked" : "" %>>
                    <label class="form-check-label" for="flexRadioDefault2">Female</label>
                </div>
            </div>
        </div>

        <div class="row mt-3 text-center">
            <div class="col-md-6">
                <button type="submit" class="btn btn-success text-center" >Update</button>

            </div>

            <div class="col-md-6">
                <button type="reset" class="btn btn-danger text-center" >Reset</button>

            </div>
        </div>
    </form>

    <!--end form    -->
</div>



<%@include file="footer.jsp" %>
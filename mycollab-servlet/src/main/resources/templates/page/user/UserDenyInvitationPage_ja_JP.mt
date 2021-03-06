UserDeniedPage.mt<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="height:100%;">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="icon" href="${defaultUrls.cdn_url}favicon.ico" type="image/x-icon">
<link rel="shortcut icon" href="${defaultUrls.cdn_url}favicon.ico" type="image/x-icon">
<link rel="stylesheet" type="text/css" href="${defaultUrls.cdn_url}css/cssVelocityPage.css">
<style>

#container {
    background-image: url('${defaultUrls.cdn_url}icons/footer_clouds.png');  background-repeat: no-repeat;
    background-position: bottom right;
}
</style>
<title>User deny invitation feedback page</title>
</head>
<body style="height: 100%; margin: 0; padding: 0; width: 100%;">
	<div id="container" style="height:100%;">
		#parse("templates/page/pageHeader.mt")
		<div id="body">
			<div id="spacing"></div>
			<div id="mainBody">
				<div id="title">
					<h1>Oops, so sad</h1>
				</div>
				<hr size="1" >
				<hr size="1" >

				<div>
					<p>We are sorry because you do not want to join MyCollab. Could you please drop some lines to tell reason to the inviter?
					</p>
					<div>
                    <textarea id="message" style="width:767px; height:130px" >
                    </textarea>
                	</div>
                    <div class="right">
                        <button class="v-button v-button-orangebtn" type="button" onclick="return sendEmailFeedBack();">Send</button>&nbsp;&nbsp;
                        <button class="v-button v-button-graybtn" type="button" onclick="return skip();">Skip</button>
                    </div>
                    <div id="requireMsg" style="display: none; padding: 12px 8px 8px 20px;">
                        <p><span style="color:red; font-style:italic">
                            (*) Reason: Message is empty.
                        </span></p>
                    </div>
				</div>
				
				#parse("templates/page/pageFooter_en_US.mt")
			</div>
	</div>
	<input type="hidden" id="inviterEmail" value="$!inviterEmail">
    <input type="hidden" id="url" value="$!redirectURL">
    <input type="hidden" id="toEmail" value="$!toEmail">
    <input type="hidden" id="toName" value="$!toName">
    <input type="hidden" id="inviterName" value="$!inviterName">
</body>
<script src="${defaultUrls.cdn_url}js/jquery-2.1.4.min.js"></script>
<script>
    $(document).ready(function(){
        $('#message').val("");
        $('#message').focus();
    });
    function sendEmailFeedBack(){
        $('#requireMsg').hide();
        if($('#message').val().trim() == ""){
            $('#requireMsg').show();
            return;
        }   
        var url = encodeURI($('#url').val().trim());
             $.ajax({
                  type: 'POST',
                  url: url,
                  data : {inviterEmail : $('#inviterEmail').val().trim() ,toEmail : $('#toEmail').val().trim(), message : $('#message').val().trim(),
                    toName : $('#toName').val().trim() , inviterName: $('#inviterName').val().trim()},
                  complete: function(data){
                     alert('Send email successfully');
                     window.location.assign("https://www.mycollab.com/");
                  }
                });
    }
    function skip(){
        window.location.assign("https://www.mycollab.com/");
    }
    
 </script>               
</html>
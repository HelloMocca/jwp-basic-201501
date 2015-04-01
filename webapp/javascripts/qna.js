var formList = document.querySelectorAll('.answerWrite input[type=submit]');
for ( var j=0 ; j < formList.length ; j++) {
	formList[j].addEventListener('click', writeAnswers, false);
}

function initDeleteAnswerBtn(){
	var deleteBtnList = document.querySelectorAll('.delete-answer-btn');
	for (var i = 0; i < deleteBtnList.length; i++) {
		deleteBtnList[i].addEventListener('click', deleteAnswer, false);
	}
}

initDeleteAnswerBtn();


function writeAnswers(e) {
	 e.preventDefault();
	 
	 var answerForm = e.currentTarget.form;
	 var url = "/api/addanswer.next";
	 var params = "questionId=" + answerForm[0].value + "&writer=" + answerForm[1].value + "&contents=" + answerForm[2].value;

	 var request = new XMLHttpRequest();
	 var json;
	 request.open("POST", url, true);
	 request.setRequestHeader("Content-type","application/x-www-form-urlencoded");
	 
	 request.onreadystatechange = function() {
		 if(request.readyState == 4 && request.status == 200) {
			json = JSON.parse(request.responseText);
			repaintAnswers(json);
		 }
	 }
	 request.send(params);
}

function deleteAnswer(e) {
	e.preventDefault();
	var answerId = e.currentTarget.attributes['data-answerId'].value;
	var questionId = e.currentTarget.attributes['data-questionId'].value;
	var url = "/api/delanswer.next";
	var params = "questionId="+questionId+"&answerId="+answerId;
	
	var request = new XMLHttpRequest();
	var json;
	request.open("POST", url, true);
	request.setRequestHeader("Content-type","application/x-www-form-urlencoded");
	
	request.onreadystatechange = function() {
		 if(request.readyState == 4 && request.status == 200) {
			json = JSON.parse(request.responseText);
			repaintAnswers(json);
		 }
	 }
	 request.send(params);
}

function repaintAnswers(json) {
	var i;
	var clone;
	var el = document.querySelector(".comments h3");
		el.innerHTML = "댓글 수 : "+json.question.countOfComment;
		el = document.querySelectorAll(".comment");
	var elLength = el.length;	
	for (i = elLength-1; i >= 0; i--) {
		el[i].parentNode.removeChild(el[i]);
	}
	var commentTemplate = document.querySelector("#commentTemplate").content;
	el = document.querySelector(".comments");
	for (i = 0; i < json.question.countOfComment; i++) {
		clone = document.importNode(commentTemplate, true);
		clone.querySelector(".comment-author").innerHTML = json.answers[i].writer+",";
		clone.querySelector(".comment-date").innerHTML = json.answers[i].createdDate;
		clone.querySelector(".comment-content").innerHTML = "<div class='about'>내용 :</div>"+json.answers[i].contents;
		clone.querySelector(".delete-answer-btn").setAttribute("data-answerId",json.answers[i].answerId);
		clone.querySelector(".delete-answer-btn").setAttribute("data-questionId",json.question.questionId);
		el.appendChild(clone);
	}
	initDeleteAnswerBtn();
}


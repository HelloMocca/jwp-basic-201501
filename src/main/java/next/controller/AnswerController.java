package next.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import next.dao.AnswerDao;
import next.dao.QuestionDao;
import next.model.Answer;
import next.model.Question;
import core.mvc.AbstractController;
import core.mvc.ModelAndView;

public class AnswerController extends AbstractController {
	private AnswerDao answerDao = new AnswerDao();
	private QuestionDao questionDao = new QuestionDao();
	
	@Override
	public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<Answer> answers;
		Question question;
		long questionId = Long.parseLong(request.getParameter("questionId"));
		answerDao.insert(new Answer(request.getParameter("writer"), request.getParameter("contents"), questionId));
		answers = answerDao.findAllByQuestionId(questionId);
		question = questionDao.findById(questionId);
		ModelAndView mav = jsonView();
		mav.addObject("answers", answers);
		mav.addObject("question", question);
		return mav;
	}

}

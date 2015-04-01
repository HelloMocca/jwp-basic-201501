package next.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import next.dao.AnswerDao;
import next.model.Answer;
import core.mvc.AbstractController;
import core.mvc.ModelAndView;

public class AnswerController extends AbstractController {
	private AnswerDao answerDao = new AnswerDao();
	private List<Answer> answers;
	private static final Logger logger = LoggerFactory.getLogger(AnswerController.class);

	@Override
	public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long questionId = Long.parseLong(request.getParameter("questionId"));
		answerDao.insert(new Answer(request.getParameter("writer"), request.getParameter("contents"), questionId));
		answers = answerDao.findAllByQuestionId(questionId);
		ModelAndView mav = jsonView();
		logger.debug("doInsert");
		mav.addObject("answers", answers);
		return mav;
	}

}

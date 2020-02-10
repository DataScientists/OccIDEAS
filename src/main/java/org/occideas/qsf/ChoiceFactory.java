package org.occideas.qsf;

import org.occideas.qsf.payload.Choice;
import org.occideas.vo.PossibleAnswerVO;

import java.util.function.Function;

public enum ChoiceFactory {

    FREETEXT("P_freetext", new Function<ChoiceFactory.AnswerDecorator,Choice>() {
        @Override
        public Choice apply(AnswerDecorator answerDecorator) {
            Choice choice = new Choice(answerDecorator.getName().substring(0, 4) + "_" +
                    answerDecorator.answerVO.getNumber() + " " + answerDecorator.answerVO.getName());
            choice.setTextEntry("true");
            return choice;
        }
    }),FREQSHIFTHOURS("P_frequencyshifthours", new Function<ChoiceFactory.AnswerDecorator,Choice>() {
        @Override
        public Choice apply(AnswerDecorator answerDecorator) {
            Choice choice = new Choice(answerDecorator.getName().substring(0, 4) + "_" +
                    answerDecorator.answerVO.getNumber() + " " + answerDecorator.answerVO.getName());
            choice.setTextEntry("true");
            return choice;
        }
    }),FREQHOURSMIN("P_frequencyhoursminute", new Function<ChoiceFactory.AnswerDecorator,Choice>() {
        @Override
        public Choice apply(AnswerDecorator answerDecorator) {
            Choice choice = new Choice(answerDecorator.getName().substring(0, 4) + "_" +
                    answerDecorator.answerVO.getNumber() + " " + answerDecorator.answerVO.getName());
            choice.setTextEntry("true");
            return choice;
        }
    }),FREQSECONDS("P_frequencyseconds", new Function<ChoiceFactory.AnswerDecorator,Choice>() {
        @Override
        public Choice apply(AnswerDecorator answerDecorator) {
            Choice choice = new Choice(answerDecorator.getName().substring(0, 4) + "_" +
                    answerDecorator.answerVO.getNumber() + " " + answerDecorator.answerVO.getName());
            choice.setTextEntry("true");
            return choice;
        }
    });

    private static class AnswerDecorator{
        private PossibleAnswerVO answerVO;
        private String name;

        public AnswerDecorator(PossibleAnswerVO answerVO, String name) {
            this.answerVO = answerVO;
            this.name = name;
        }

        public PossibleAnswerVO getAnswerVO() {
            return answerVO;
        }

        public void setAnswerVO(PossibleAnswerVO answerVO) {
            this.answerVO = answerVO;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    ChoiceFactory(String type, Function<AnswerDecorator, Choice> converter) {
        this.type = type;
        this.converter = converter;
    }

    private String type;
    private Function<AnswerDecorator, Choice> converter;

    public static Choice create(PossibleAnswerVO answerVO,String name){
        for(ChoiceFactory choiceFactory:ChoiceFactory.values()){
            if(choiceFactory.type.equals(answerVO.getType())){
                return choiceFactory.converter.apply(new AnswerDecorator(answerVO,name));
            }
        }
        return new Choice(name.substring(0, 4) + "_" +
                answerVO.getNumber() + " " + answerVO.getName());
    }

}

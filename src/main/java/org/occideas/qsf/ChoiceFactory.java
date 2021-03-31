package org.occideas.qsf;

import org.apache.commons.lang3.StringUtils;
import org.occideas.entity.Constant;
import org.occideas.qsf.payload.Choice;
import org.occideas.vo.PossibleAnswerVO;

import java.util.function.Function;

public enum ChoiceFactory {

    FREETEXT("P_freetext", new Function<ChoiceFactory.AnswerDecorator,Choice>() {
        @Override
        public Choice apply(AnswerDecorator answerDecorator) {
            String choiceKey = answerDecorator.getName().substring(0, 4) + "_" + answerDecorator.answerVO.getNumber() + " ";
            String hiddenChoiceKey = Constant.SPAN_START_DISPLAY_NONE + choiceKey + Constant.SPAN_END;
            String answerText = StringUtils.isBlank(answerDecorator.translation) ? answerDecorator.answerVO.getName() : answerDecorator.translation;
            Choice choice = new Choice((answerDecorator.isHideNodeKeys() ? hiddenChoiceKey : choiceKey) +
                    answerText);
            choice.setTextEntry("true");
            return choice;
        }
    }),FREQSHIFTHOURS("P_frequencyshifthours", new Function<ChoiceFactory.AnswerDecorator,Choice>() {
        @Override
        public Choice apply(AnswerDecorator answerDecorator) {
            String choiceKey = answerDecorator.getName().substring(0, 4) + "_" + answerDecorator.answerVO.getNumber() + " ";
            String hiddenChoiceKey = Constant.SPAN_START_DISPLAY_NONE + choiceKey + Constant.SPAN_END;
            String answerText = StringUtils.isBlank(answerDecorator.translation) ? answerDecorator.answerVO.getName() : answerDecorator.translation;
            Choice choice = new Choice((answerDecorator.isHideNodeKeys() ? hiddenChoiceKey : choiceKey) +
                    answerText);
            choice.setTextEntry("true");
            return choice;
        }
    }),FREQHOURSMIN("P_frequencyhoursminute", new Function<ChoiceFactory.AnswerDecorator,Choice>() {
        @Override
        public Choice apply(AnswerDecorator answerDecorator) {
            String choiceKey = answerDecorator.getName().substring(0, 4) + "_" + answerDecorator.answerVO.getNumber() + " ";
            String hiddenChoiceKey = Constant.SPAN_START_DISPLAY_NONE + choiceKey + Constant.SPAN_END;
            String answerText = StringUtils.isBlank(answerDecorator.translation) ? answerDecorator.answerVO.getName() : answerDecorator.translation;
            Choice choice = new Choice((answerDecorator.isHideNodeKeys() ? hiddenChoiceKey : choiceKey) +
                    answerText);
            choice.setTextEntry("true");
            return choice;
        }
    }),FREQSECONDS("P_frequencyseconds", new Function<ChoiceFactory.AnswerDecorator,Choice>() {
        @Override
        public Choice apply(AnswerDecorator answerDecorator) {
            String choiceKey = answerDecorator.getName().substring(0, 4) + "_" + answerDecorator.answerVO.getNumber() + " ";
            String hiddenChoiceKey = Constant.SPAN_START_DISPLAY_NONE + choiceKey + Constant.SPAN_END;
            String answerText = StringUtils.isBlank(answerDecorator.translation) ? answerDecorator.answerVO.getName() : answerDecorator.translation;
            Choice choice = new Choice((answerDecorator.isHideNodeKeys() ? hiddenChoiceKey : choiceKey) +
                    answerText);
            choice.setTextEntry("true");
            return choice;
        }
    }),FREQWEEKS("P_frequencyweeks", new Function<ChoiceFactory.AnswerDecorator,Choice>() {
        @Override
        public Choice apply(AnswerDecorator answerDecorator) {
            String choiceKey = answerDecorator.getName().substring(0, 4) + "_" + answerDecorator.answerVO.getNumber() + " ";
            String hiddenChoiceKey = Constant.SPAN_START_DISPLAY_NONE + choiceKey + Constant.SPAN_END;
            String answerText = StringUtils.isBlank(answerDecorator.translation) ? answerDecorator.answerVO.getName() : answerDecorator.translation;
            Choice choice = new Choice((answerDecorator.isHideNodeKeys() ? hiddenChoiceKey : choiceKey) +
                    answerText);
            return choice;
        }
    });

    private static class AnswerDecorator{
        private PossibleAnswerVO answerVO;
        private String name;
        private boolean hideNodeKeys;
        private String translation;

        public AnswerDecorator(PossibleAnswerVO answerVO, String name, boolean hideNodeKeys, String translation) {
            this.answerVO = answerVO;
            this.name = name;
            this.hideNodeKeys = hideNodeKeys;
            this.translation = translation;
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

        public boolean isHideNodeKeys() {
            return hideNodeKeys;
        }

        public void setHideNodeKeys(boolean hideNodeKeys) {
            this.hideNodeKeys = hideNodeKeys;
        }
    }

    ChoiceFactory(String type, Function<AnswerDecorator, Choice> converter) {
        this.type = type;
        this.converter = converter;
    }

    private String type;
    private Function<AnswerDecorator, Choice> converter;

    public static Choice create(PossibleAnswerVO answerVO,String name, boolean hideNodeKeys, String translation){
        for(ChoiceFactory choiceFactory:ChoiceFactory.values()){
            if(choiceFactory.type.equals(answerVO.getType())){
                return choiceFactory.converter.apply(new AnswerDecorator(answerVO, name, hideNodeKeys, translation));
            }
        }
        String choiceKey = name.substring(0, 4) + "_" + answerVO.getNumber() + " ";
        String hiddenChoiceKey = Constant.SPAN_START_DISPLAY_NONE + choiceKey + Constant.SPAN_END;
        String answerText = StringUtils.isBlank(translation) ? answerVO.getName() : translation;
        return new Choice((hideNodeKeys ? hiddenChoiceKey : choiceKey) + answerText);
    }

}

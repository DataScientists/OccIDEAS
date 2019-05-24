package org.occideas.vo;

public class FragmentVODecorator {

  private FragmentVO fragmentVO;
  private boolean created;

  public FragmentVODecorator() {
  }

  public FragmentVODecorator(FragmentVO fragmentVO, boolean created) {
    super();
    this.fragmentVO = fragmentVO;
    this.created = created;
  }

  public FragmentVO getFragmentVO() {
    return fragmentVO;
  }

  public void setFragmentVO(FragmentVO fragmentVO) {
    this.fragmentVO = fragmentVO;
  }

  public boolean isCreated() {
    return created;
  }

  public void setCreated(boolean created) {
    this.created = created;
  }

}

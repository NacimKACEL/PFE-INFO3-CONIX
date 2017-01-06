import { PFECONIXINFO3Page } from './app.po';

describe('pfe-conix-info3 App', function() {
  let page: PFECONIXINFO3Page;

  beforeEach(() => {
    page = new PFECONIXINFO3Page();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});

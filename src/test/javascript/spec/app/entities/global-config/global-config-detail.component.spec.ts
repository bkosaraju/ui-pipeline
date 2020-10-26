import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PipelineTestModule } from '../../../test.module';
import { GlobalConfigDetailComponent } from 'app/entities/global-config/global-config-detail.component';
import { GlobalConfig } from 'app/shared/model/global-config.model';

describe('Component Tests', () => {
  describe('GlobalConfig Management Detail Component', () => {
    let comp: GlobalConfigDetailComponent;
    let fixture: ComponentFixture<GlobalConfigDetailComponent>;
    const route = ({ data: of({ globalConfig: new GlobalConfig(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PipelineTestModule],
        declarations: [GlobalConfigDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(GlobalConfigDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(GlobalConfigDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load globalConfig on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.globalConfig).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});

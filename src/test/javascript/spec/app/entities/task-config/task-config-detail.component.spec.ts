import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PipelineTestModule } from '../../../test.module';
import { TaskConfigDetailComponent } from 'app/entities/task-config/task-config-detail.component';
import { TaskConfig } from 'app/shared/model/task-config.model';

describe('Component Tests', () => {
  describe('TaskConfig Management Detail Component', () => {
    let comp: TaskConfigDetailComponent;
    let fixture: ComponentFixture<TaskConfigDetailComponent>;
    const route = ({ data: of({ taskConfig: new TaskConfig(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PipelineTestModule],
        declarations: [TaskConfigDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(TaskConfigDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TaskConfigDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load taskConfig on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.taskConfig).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
